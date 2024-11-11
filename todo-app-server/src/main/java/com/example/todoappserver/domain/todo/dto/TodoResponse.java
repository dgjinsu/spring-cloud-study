package com.example.todoappserver.domain.todo.dto;

import lombok.Builder;

@Builder
public record TodoResponse(
        Long todoId,
        String content,
        Boolean isCompleted,
        String createdAt
) {
}
