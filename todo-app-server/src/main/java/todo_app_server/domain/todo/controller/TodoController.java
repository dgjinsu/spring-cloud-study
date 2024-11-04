package todo_app_server.domain.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo_app_server.domain.todo.dto.*;
import todo_app_server.domain.todo.service.TodoService;
import todo_app_server.global.common.Response;

@RestController
@RequestMapping("/todo-app/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @Operation(summary = "TODO 저장")
    @PostMapping("")
    public ResponseEntity<Response<TodoResponse>> saveTodo(@RequestBody TodoSaveRequest request) {
        return ResponseEntity.ok(new Response<TodoResponse>(todoService.saveTodo(request), "TODO 저장 완료"));
    }

    @Operation(summary = "TODO 리스트 조회")
    @GetMapping("")
    public ResponseEntity<Response<TodoListResponse>> getTodoList() {
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
        return ResponseEntity.ok(new Response<Void>("완료된 TODO 제거 완료"));
    }
}
