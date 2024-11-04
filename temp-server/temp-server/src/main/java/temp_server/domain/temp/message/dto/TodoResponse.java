package temp_server.domain.temp.message.dto;

import lombok.Builder;

@Builder
public record TodoResponse(
        Long todoId,
        String content,
        Boolean isCompleted
) {
}
