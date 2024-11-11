package com.example.todoappserver.domain.todo.dto;

import com.example.todoappserver.domain.todo.dto.member.MemberInfoResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record TodoListResponse(
        List<TodoResponse> todoResponseList,
        Integer totalTodoNum,
        Integer completedTodoNum,
        MemberInfoResponse memberInfoResponse
) {
}
