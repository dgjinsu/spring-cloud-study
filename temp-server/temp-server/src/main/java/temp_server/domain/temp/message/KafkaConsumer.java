package temp_server.domain.temp.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import temp_server.domain.temp.message.dto.CreatedAtInfoRequest;
import temp_server.domain.temp.message.dto.CreatedAtInfoResponse;
import temp_server.domain.temp.message.dto.TodoResponse;
import temp_server.domain.temp.message.dto.TodoSaveRequest;
import temp_server.domain.temp.service.TempService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final TempService tempService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = KafkaConstants.TODO_SAVE_TOPIC, groupId = "temp-group", containerFactory = "kafkaListenerContainerFactory")
    public void saveTemp(TodoSaveRequest request) {
        try {
            tempService.saveTemp(request);
            log.info("temp consumer processed request: {}", request);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @KafkaListener(topics = KafkaConstants.TODO_REQUEST_TOPIC, groupId = KafkaConstants.TEMP_GROUP)
    public void handleCreatedAtRequest(CreatedAtInfoRequest request) {
        try {
            Map<Long, LocalDateTime> createdAtMap = tempService.getCreatedAtByTodoIds(request.getTodoIds());

            List<CreatedAtInfoResponse> responses = createdAtMap.entrySet().stream()
                    .map(entry -> new CreatedAtInfoResponse(entry.getKey(), entry.getValue().toString()))
                    .collect(Collectors.toList());

            kafkaTemplate.send(KafkaConstants.TODO_RESPONSE_TOPIC, objectMapper.writeValueAsString(responses));
            log.info("Sent bulk createdAt info for todoIds: {}", request.getTodoIds());
        } catch (Exception e) {
            log.error("Error processing bulk createdAt request", e);
        }
    }
}
