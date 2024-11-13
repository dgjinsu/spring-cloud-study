package com.example.todoappserver.domainold.todo.dto;

import com.example.todoappserver.domainold.todo.dto.member.MemberInfoResponse;
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
