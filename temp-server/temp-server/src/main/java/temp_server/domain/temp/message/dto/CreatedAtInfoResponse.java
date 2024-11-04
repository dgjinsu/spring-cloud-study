package temp_server.domain.temp.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedAtInfoResponse {
    private Long todoId;
    private String createdAt;
}