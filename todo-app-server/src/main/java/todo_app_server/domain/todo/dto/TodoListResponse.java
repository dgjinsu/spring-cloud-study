package todo_app_server.domain.todo.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TodoListResponse(
        List<TodoResponse> todoResponseList,
        Integer totalTodoNum,
        Integer completedTodoNum
) {
}
