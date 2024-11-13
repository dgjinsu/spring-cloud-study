package com.example.todoappserver.global.message.dto;

public record TodoSaveRequestDto(
        Long todoId,
        String content) {
}
