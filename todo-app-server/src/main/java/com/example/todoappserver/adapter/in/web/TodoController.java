package com.example.todoappserver.adapter.in.web;

import com.example.common.annotation.LoginMember;
import com.example.common.annotation.MemberRole;
import com.example.todoappserver.domainold.todo.dto.TodoListResponse;
import com.example.todoappserver.domainold.todo.dto.TodoResponse;
import com.example.todoappserver.domainold.todo.dto.TodoSaveRequest;
import com.example.todoappserver.domainold.todo.dto.TodoUpdateRequest;
import com.example.todoappserver.domainold.todo.service.TodoService;
import com.example.todoappserver.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo-app/api/v1/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;

    @Operation(summary = "TODO 저장")
    @PostMapping("")
    public ResponseEntity<Response<TodoResponse>> saveTodo(@RequestBody TodoSaveRequest request) {
        return ResponseEntity.ok(new Response<TodoResponse>(todoService.saveTodo(request), "TODO 저장 완료"));
    }

//    @RequireRole("ROLE_MEMBER")
//    @AdminRole
    @MemberRole
    @Operation(summary = "TODO 리스트 조회")
    @GetMapping("")
    public ResponseEntity<Response<TodoListResponse>> getTodoList(@LoginMember String loginId) {
        return ResponseEntity.ok(new Response<TodoListResponse>(todoService.getTodoList(), "TODO 리스트 조회 완료"));
    }

    @Operation(summary = "TODO 수정")
    @PatchMapping("/{todoId}")
    public ResponseEntity<Response<TodoResponse>> updateTodo(@PathVariable Long todoId, @RequestBody TodoUpdateRequest request) {
        return ResponseEntity.ok(new Response<TodoResponse>(todoService.updateTodo(todoId, request), "TODO 내용 수정 완료"));
    }

    @Operation(summary = "완료된 TODO 제거")
    @DeleteMapping("/completed")
    public ResponseEntity<Response<Void>> deleteCompletedTodo() {
        todoService.deleteCompletedTodo();
        return ResponseEntity.ok(new Response<Void>("완료된 TODO 제거 완료"));
    }

    @Operation(summary = "TODO 개별 제거")
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Response<Void>> deleteTodoById(@PathVariable Long todoId) {
        todoService.deleteTodoById(todoId);
        return ResponseEntity.ok(new Response<Void>("TODO 개별 제거 완료"));
    }
}
