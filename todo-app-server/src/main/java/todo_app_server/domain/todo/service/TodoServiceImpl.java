package todo_app_server.domain.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo_app_server.domain.todo.dto.*;
import todo_app_server.domain.todo.entity.Todo;
import todo_app_server.domain.todo.message.KafkaProducer;
import todo_app_server.domain.todo.message.KafkaConsumer;
import todo_app_server.domain.todo.repository.TodoRepository;
import todo_app_server.global.exception.ErrorCode;
import todo_app_server.global.exception.TodoAppException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final KafkaProducer kafkaProducer;
    private final KafkaConsumer kafkaConsumer;

    /**
     * 아이템 저장
     */
    @Override
    public TodoResponse saveTodo(TodoSaveRequest request) {
        Todo todo = todoMapper.toEntity(request);
        Todo savedTodo = todoRepository.save(todo);

        TodoResponse response = todoMapper.toResponse(savedTodo);

        // Kafka로 메시지 발행
        kafkaProducer.send("todo-save-topic", response);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TodoListResponse getTodoList() {
        List<Todo> todoList = todoRepository.findAll();
        List<Long> todoIds = todoList.stream().map(Todo::getId).collect(Collectors.toList());

        // Bulk로 Kafka에 요청을 보내고 CompletableFuture로 응답을 받음
        CompletableFuture<List<TodoResponse>> futureResponses = kafkaConsumer.listenForBulkCreatedAtInfo(todoIds);
        kafkaProducer.requestBulkCreatedAtInfo("todo-request-topic", todoIds);

        // 모든 CompletableFuture가 완료될 때까지 대기하여 bulk 응답 수신
        List<TodoResponse> todoResponseList = futureResponses.join();

        // 응답된 todoResponseList와 기존의 todoList를 매칭하여 content와 isCompleted 필드를 채워 넣음
        List<TodoResponse> updatedTodoResponseList = todoResponseList.stream()
                .map(response -> todoList.stream()
                        .filter(todo -> todo.getId().equals(response.todoId()))
                        .findFirst()
                        .map(todo -> TodoResponse.builder()
                                .todoId(response.todoId())
                                .content(todo.getContent())
                                .isCompleted(todo.getIsCompleted())
                                .createdAt(response.createdAt())
                                .build())
                        .orElse(response) // 일치하는 todo가 없으면 기존 response 반환
                )
                .collect(Collectors.toList());

        // 응답된 todoResponseList를 TodoListResponse로 변환하여 반환
        return TodoListResponse.builder()
                .todoResponseList(updatedTodoResponseList)
                .totalTodoNum(updatedTodoResponseList.size())
                .completedTodoNum((int) updatedTodoResponseList.stream().filter(TodoResponse::isCompleted).count())
                .build();
    }

    /**
     * 아이템 업데이트
     */
    @Override
    public TodoResponse updateTodo(Long todoId, TodoUpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoAppException(ErrorCode.TODO_NOT_FOUND));

        todo.updateTodo(request);

        return todoMapper.toResponse(todo);
    }

    /**
     * 완료된 아이템 제거
     */
    @Override
    public void deleteCompletedTodo() {
        int deletedTodoCnt = todoRepository.deleteCompletedTodo();
        log.info("제거된 TODO 수: {}", deletedTodoCnt);
    }

    /**
     * 하나의 아이템 제거
     */
    @Override
    public void deleteTodoById(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}