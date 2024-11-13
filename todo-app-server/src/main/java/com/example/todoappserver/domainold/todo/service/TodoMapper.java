package com.example.todoappserver.domainold.todo.service;

import com.example.todoappserver.domainold.todo.dto.TodoListResponse;
import com.example.todoappserver.domainold.todo.dto.TodoResponse;
import com.example.todoappserver.domainold.todo.dto.TodoSaveRequest;
import com.example.todoappserver.domainold.todo.dto.member.MemberInfoResponse;
import com.example.todoappserver.domainold.todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // bean 으로 관리
public interface TodoMapper {

    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    @Mapping(target = "isCompleted", constant = "false")
    Todo toEntity(TodoSaveRequest request);

    @Mapping(source = "id", target = "todoId")
    @Mapping(target = "isCompleted", source = "isCompleted")  // isCompleted 필드 매핑 명시
    TodoResponse toResponse(Todo todo);

    default TodoListResponse toResponseList(List<Todo> todoList, MemberInfoResponse memberInfoResponse) {
        List<TodoResponse> todoResponseList = todoList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        int totalTodoNum = todoResponseList.size();
        int completedTodoNum = (int) todoResponseList.stream()
                .filter(TodoResponse::isCompleted)
                .count();

        return TodoListResponse.builder()
                .todoResponseList(todoResponseList)
                .totalTodoNum(totalTodoNum)
                .completedTodoNum(completedTodoNum)
                .memberInfoResponse(memberInfoResponse)
                .build();
    }
}
