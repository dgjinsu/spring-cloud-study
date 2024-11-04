package todo_app_server.domain.todo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import todo_app_server.domain.todo.dto.TodoListResponse;
import todo_app_server.domain.todo.dto.TodoResponse;
import todo_app_server.domain.todo.dto.TodoSaveRequest;
import todo_app_server.domain.todo.entity.Todo;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoMapperTest {
    /**
     * boolean 타입과 @Getter 어노테이션을 함께 사용하면 안 되는 이유
     * https://hyem5019.tistory.com/m/entry/boolean-%EB%B0%94%EC%9D%B8%EB%94%A9-%EC%97%90%EB%9F%AC-boolean%EA%B3%BC-Boolean%EC%9D%98-%EC%B0%A8%EC%9D%B4
     */

    private TodoMapper todoMapper;

    @BeforeEach
    void setUp() {
        todoMapper = TodoMapper.INSTANCE;
    }

    @Test
    void toEntityTest() {
        // given
        TodoSaveRequest request = new TodoSaveRequest("Test Todo");

        // when
        Todo todo = todoMapper.toEntity(request);

        // then
        assertThat(todo).isNotNull();
        assertThat(todo.getContent()).isEqualTo("Test Todo");
        assertThat(todo.getIsCompleted()).isFalse();
    }

    @Test
    void toResponseTest() {
        // given
        Long todoId = 1L;
        String content = "Test Todo";
        Todo todo = Todo.builder()
                .content(content)
                .isCompleted(true)
                .build();
        ReflectionTestUtils.setField(todo, "id", todoId);

        // when
        TodoResponse response = todoMapper.toResponse(todo);

        // then
        assertThat(response).isNotNull();
        assertThat(response.todoId()).isEqualTo(todoId);
        assertThat(response.content()).isEqualTo(content);
        assertThat(response.isCompleted()).isTrue();
    }

    @Test
    void toResponseListTest() {
        // given
        Long todoId1 = 1L;
        String content1 = "Test Todo1";
        Todo todo1 = Todo.builder()
                .content(content1)
                .isCompleted(false)
                .build();
        ReflectionTestUtils.setField(todo1, "id", todoId1);

        Long todoId2 = 2L;
        String content2 = "Test Todo2";
        Todo todo2 = Todo.builder()
                .content(content2)
                .isCompleted(true)
                .build();
        ReflectionTestUtils.setField(todo2, "id", todoId2);

        List<Todo> todoList = Arrays.asList(todo1, todo2);

        // when
        TodoListResponse responseList = todoMapper.toResponseList(todoList);

        // then
        assertThat(responseList).isNotNull();
        assertThat(responseList.todoResponseList()).hasSize(2);

        assertThat(responseList.todoResponseList().get(0).todoId()).isEqualTo(todoId1);
        assertThat(responseList.todoResponseList().get(0).content()).isEqualTo(content1);
        assertThat(responseList.todoResponseList().get(0).isCompleted()).isFalse();
        assertThat(responseList.todoResponseList().get(1).todoId()).isEqualTo(todoId2);
        assertThat(responseList.todoResponseList().get(1).content()).isEqualTo(content2);
        assertThat(responseList.todoResponseList().get(1).isCompleted()).isTrue();

        assertThat(responseList.totalTodoNum()).isEqualTo(2);
        assertThat(responseList.completedTodoNum()).isEqualTo(1);
    }
}
