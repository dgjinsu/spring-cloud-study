package com.example.todoappserver.domain.todo.message.dto;

import lombok.Builder;

@Builder
public record TodoResponseDto(
        Long todoId,
        String content,
        Boolean isCompleted,
        String createdAt
) {
}
