package todo_app_server.domain.todo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import todo_app_server.domain.todo.dto.TodoSaveRequest;
import todo_app_server.domain.todo.dto.TodoUpdateRequest;
import todo_app_server.domain.todo.entity.Todo;
import todo_app_server.domain.todo.repository.TodoRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers // tc 이용 명시
@ActiveProfiles("test") // test profile
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 DB(h2)가 아닌 test profile 에 정의된 db 사용
class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll(); // 테스트 전에 DB 초기화
    }


    // postgres 컨테이너 생성
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testDB")
            .withUsername("test")
            .withPassword("test");

    // properties 설정
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void saveTodoTest() throws Exception {
        // given
        TodoSaveRequest request = new TodoSaveRequest("Test Todo");

        // when & then
        mockMvc.perform(post("/api/v1/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TODO 저장 완료"))
                .andExpect(jsonPath("$.data.todoId").exists())
                .andExpect(jsonPath("$.data.content").value(request.content()))
                .andExpect(jsonPath("$.data.isCompleted").value(false));
    }


    @Test
    void getTodoListTest() throws Exception {
        // given
        Todo todo = Todo.builder().content("Test Todo").isCompleted(false).build();
        todoRepository.save(todo);

        // when & then
        mockMvc.perform(get("/api/v1/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TODO 리스트 조회 완료"))
                .andExpect(jsonPath("$.data.todoResponseList[0].todoId").exists())
                .andExpect(jsonPath("$.data.todoResponseList[0].content").value("Test Todo"))
                .andExpect(jsonPath("$.data.todoResponseList[0].isCompleted").value(false));
    }

    @Test
    void updateTodoTest() throws Exception {
        // given
        Todo todo = Todo.builder().content("Old Content").isCompleted(false).build();
        todo = todoRepository.save(todo);

        TodoUpdateRequest request = new TodoUpdateRequest("Updated Content", true);

        // when & then
        mockMvc.perform(patch("/api/v1/todos/" + todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("TODO 내용 수정 완료"))
                .andExpect(jsonPath("$.data.todoId").exists())
                .andExpect(jsonPath("$.data.content").value("Updated Content"))
                .andExpect(jsonPath("$.data.isCompleted").value(true));
    }

    @Test
    void deleteCompletedTodoTest() throws Exception {
        // given
        Todo completedTodo = Todo.builder().content("Completed Todo").isCompleted(true).build();
        Todo incompleteTodo = Todo.builder().content("Incomplete Todo").isCompleted(false).build();
        todoRepository.save(completedTodo);
        todoRepository.save(incompleteTodo);

        // when & then
        mockMvc.perform(delete("/api/v1/todos/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("완료된 TODO 제거 완료"));

        mockMvc.perform(get("/api/v1/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.todoResponseList.length()").value(1))
                .andExpect(jsonPath("$.data.todoResponseList[0].content").value("Incomplete Todo"))
                .andExpect(jsonPath("$.data.todoResponseList[0].isCompleted").value(false));
    }

    @Test
    void deleteTodoByIdTest() throws Exception {
        // given
        Todo todo = Todo.builder().content("Test Todo").isCompleted(false).build();
        todo = todoRepository.save(todo);

        // when & then
        mockMvc.perform(delete("/api/v1/todos/" + todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("완료된 TODO 제거 완료"));

        mockMvc.perform(get("/api/v1/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.todoResponseList").isEmpty());
    }
}
