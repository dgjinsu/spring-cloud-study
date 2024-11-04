package todo_app_server.domain.todo.message.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedAtInfoRequest {
    private List<Long> todoIds;
}