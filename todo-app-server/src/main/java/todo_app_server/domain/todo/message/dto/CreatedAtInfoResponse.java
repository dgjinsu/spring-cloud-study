package todo_app_server.domain.todo.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedAtInfoResponse {
    private Long todoId;
    private String createdAt;
}