package todo_app_server.domain.todo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import todo_app_server.domain.todo.dto.TodoListResponse;
import todo_app_server.domain.todo.dto.TodoResponse;
import todo_app_server.domain.todo.dto.TodoSaveRequest;
import todo_app_server.domain.todo.dto.TodoUpdateRequest;
import todo_app_server.domain.todo.entity.Todo;
import todo_app_server.domain.todo.repository.TodoRepository;
import todo_app_server.global.exception.ErrorCode;
import todo_app_server.global.exception.TodoAppException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;
//    인터페이스 spy 해도 어차피 stub해야하는데 왜 나누지

    @InjectMocks
    private TodoServiceImpl todoService;

    // TodoMapper를 실제 인스턴스로 주입
    private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

    @BeforeEach
    void setUp() {
        // @InjectMocks는 자동으로 @Mock이나 @Spy로 주입된 의존성만 주입 가능
        // 따라서 수동으로 주입해줘야 함
        ReflectionTestUtils.setField(todoService, "todoMapper", todoMapper);
    }

    @Test
    @DisplayName("아이템 저장 성공 테스트")
    void saveTodoTest() {
        // given
        TodoSaveRequest request = TodoSaveRequest.builder().build();
        Todo todo = todoMapper.toEntity(request);
        ReflectionTestUtils.setField(todo, "id", 1L);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse todoResponse = todoService.saveTodo(request);

        // then
        assertEquals(todo.getId(), todoResponse.todoId());
        assertEquals(todo.getContent(), todoResponse.content());
        assertEquals(todo.getIsCompleted(), todoResponse.isCompleted());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("아이템 목록 조회 테스트")
    void getTodoListTest() {
        // given
        Todo todo1 = new Todo("Test Todo 1", false);
        Todo todo2 = new Todo("Test Todo 2", true);
        ReflectionTestUtils.setField(todo1, "id", 1L);
        ReflectionTestUtils.setField(todo1, "id", 2L);
        List<Todo> todoList = Arrays.asList(todo1, todo2);

        when(todoRepository.findAll()).thenReturn(todoList);

        // when
        TodoListResponse response = todoService.getTodoList();

        // then
        assertEquals(2, response.todoResponseList().size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("아이템 업데이트 테스트")
    void updateTodoTest() {
        // given
        Long todoId = 1L;
        TodoUpdateRequest request = new TodoUpdateRequest("Updated Todo", true);
        Todo todo = new Todo("Test Todo", false);
        ReflectionTestUtils.setField(todo, "id", todoId);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        // when
        todoService.updateTodo(todoId, request);

        // then
        assertEquals(request.content(), todo.getContent());
        assertEquals(request.isComplete(), todo.getIsCompleted());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("아이템 업데이트 예외 테스트")
    void updateTodoException() {
        // given
        Long todoId = 1L;
        TodoUpdateRequest request = new TodoUpdateRequest("Updated Todo", true);

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // when
        TodoAppException exception = assertThrows(TodoAppException.class,
                () -> todoService.updateTodo(todoId, request));

        // then
        assertEquals(ErrorCode.TODO_NOT_FOUND, exception.getErrorName());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    @DisplayName("완료된 아이템 제거 테스트")
    void deleteTodoTest() {
        // given
        when(todoRepository.deleteCompletedTodo()).thenReturn(2);

        // when
        todoService.deleteCompletedTodo();

        // then
        verify(todoRepository, times(1)).deleteCompletedTodo();
    }

    @Test
    @DisplayName("하나의 아이템 제거 테스트")
    void deleteTodoByIdTest() {
        // given
        Long todoId = 1L;

        // when
        todoService.deleteTodoById(todoId);

        // then
        verify(todoRepository, times(1)).deleteById(todoId);
    }
}
