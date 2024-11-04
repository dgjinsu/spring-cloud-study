package todo_app_server.domain.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import todo_app_server.domain.todo.dto.TodoListResponse;
import todo_app_server.domain.todo.dto.TodoResponse;
import todo_app_server.domain.todo.dto.TodoSaveRequest;
import todo_app_server.domain.todo.dto.TodoUpdateRequest;
import todo_app_server.domain.todo.service.TodoService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("TODO 저장")
    void saveTodoTest() throws Exception {
        // given
        TodoSaveRequest request = TodoSaveRequest.builder().build();
        TodoResponse todoResponse = TodoResponse.builder().build();
        when(todoService.saveTodo(any(TodoSaveRequest.class))).thenReturn(todoResponse);

        // when & then
        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("TODO 저장 완료"));

        verify(todoService, times(1)).saveTodo(request);
    }

    @Test
    @DisplayName("TODO 리스트 조회")
    void getTodoListTest() throws Exception {
        // given
        TodoListResponse todoListResponse = TodoListResponse.builder().build();
        when(todoService.getTodoList()).thenReturn(todoListResponse);

        // when & then
        mockMvc.perform(get("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").value("TODO 리스트 조회 완료"));

        verify(todoService, times(1)).getTodoList();
    }

    @Test
    @DisplayName("TODO 수정")
    void updateTodoTest() throws Exception {
        // given
        Long todoId = 1L;
        TodoUpdateRequest request = TodoUpdateRequest.builder().build();

        // when & then
        mockMvc.perform(patch("/api/v1/todos/{todoId}", todoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TODO 내용 수정 완료"));
        // 서비스 호출 검증
        verify(todoService, times(1)).updateTodo(todoId, request);
    }

    @Test
    @DisplayName("완료된 TODO 제거")
    void deleteCompletedTodoTest() throws Exception {
        // given
        doNothing().when(todoService).deleteCompletedTodo();

        // when & then
        mockMvc.perform(delete("/api/v1/todos/completed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("완료된 TODO 제거 완료"));

        // 서비스 호출 검증
        verify(todoService, times(1)).deleteCompletedTodo();
    }

    @Test
    @DisplayName("TODO 개별 제거")
    void deleteTodoByIdTest() throws Exception {
        // given
        Long todoId = 1L;
        doNothing().when(todoService).deleteTodoById(todoId);

        // when & then
        mockMvc.perform(delete("/api/v1/todos/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("완료된 TODO 제거 완료"));

        // 서비스 호출 검증
        verify(todoService, times(1)).deleteTodoById(todoId);
    }
}
