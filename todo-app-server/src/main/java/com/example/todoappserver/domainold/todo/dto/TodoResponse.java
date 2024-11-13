package com.example.todoappserver.domainold.todo.dto;

import lombok.Builder;

@Builder
public record TodoResponse(
        Long todoId,
        String content,
        Boolean isCompleted,
        String createdAt
) {
}
