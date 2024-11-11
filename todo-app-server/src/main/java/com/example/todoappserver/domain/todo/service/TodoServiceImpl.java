package com.example.todoappserver.domain.todo.service;

import com.example.todoappserver.domain.todo.dto.TodoListResponse;
import com.example.todoappserver.domain.todo.dto.TodoResponse;
import com.example.todoappserver.domain.todo.dto.TodoSaveRequest;
import com.example.todoappserver.domain.todo.dto.TodoUpdateRequest;
import com.example.todoappserver.domain.todo.dto.member.MemberInfoResponse;
import com.example.todoappserver.domain.todo.entity.Todo;
import com.example.todoappserver.global.feign.MemberServerClient;
import com.example.todoappserver.global.feign.TempServerClient;
import com.example.todoappserver.domain.todo.message.KafkaProducer;
import com.example.todoappserver.domain.todo.repository.TodoRepository;
import com.example.todoappserver.global.exception.ErrorCode;
import com.example.todoappserver.global.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final KafkaProducer kafkaProducer;
    private final MemberServerClient memberServerClient;

    /**
     * 아이템 저장
     */
    @Override
    public TodoResponse saveTodo(TodoSaveRequest request) {
        Todo todo = todoMapper.toEntity(request);
        Todo savedTodo = todoRepository.save(todo);

        TodoResponse response = todoMapper.toResponse(savedTodo);
        log.info("todo 저장");
        // Kafka로 메시지 발행
        kafkaProducer.send("todo-save-topic", response);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TodoListResponse getTodoList() {
        List<Todo> todoList = todoRepository.findAll();
        MemberInfoResponse memberInfo = memberServerClient.getMemberInfo();
        return todoMapper.toResponseList(todoList, memberInfo);
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