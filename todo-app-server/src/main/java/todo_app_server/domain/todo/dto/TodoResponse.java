package todo_app_server.domain.todo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public record TodoResponse(
        Long todoId,
        String content,
        Boolean isCompleted,
        String createdAt
) {
}
