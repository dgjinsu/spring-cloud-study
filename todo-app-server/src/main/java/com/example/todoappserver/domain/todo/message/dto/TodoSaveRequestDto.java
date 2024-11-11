package com.example.todoappserver.domain.todo.message.dto;

public record TodoSaveRequestDto(
        Long todoId,
        String content) {
}
