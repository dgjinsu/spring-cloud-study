package com.example.todoappserver.domain.todo.service;


import com.example.todoappserver.domain.todo.dto.TodoListResponse;
import com.example.todoappserver.domain.todo.dto.TodoResponse;
import com.example.todoappserver.domain.todo.dto.TodoSaveRequest;
import com.example.todoappserver.domain.todo.dto.TodoUpdateRequest;

public interface TodoService {

    // 아이템 저장
    TodoResponse saveTodo(TodoSaveRequest request);

    // 아이템 list 조회
    TodoListResponse getTodoList();

    // 아이템 업데이트
    TodoResponse updateTodo(Long todoId, TodoUpdateRequest request);

    // 완료된 아이템 제거
    void deleteCompletedTodo();

    // 하나의 아이템 제거
    void deleteTodoById(Long todoId);
}