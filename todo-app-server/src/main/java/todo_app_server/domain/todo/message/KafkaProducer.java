package todo_app_server.domain.todo.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import todo_app_server.domain.todo.dto.TodoResponse;
import todo_app_server.domain.todo.message.dto.CreatedAtInfoRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper(); // ObjectMapper를 필드로 선언하여 재사용

    public void send(String topic, TodoResponse response) {
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("[todo] kafka send complete");
    }

    public void requestBulkCreatedAtInfo(String topic, List<Long> todoIds) {
        CreatedAtInfoRequest request = new CreatedAtInfoRequest(todoIds);
        try {
            String jsonInString = mapper.writeValueAsString(request);
            kafkaTemplate.send(topic, jsonInString);
            log.info("[todo] Kafka send complete '{}': {}", topic, jsonInString);
        } catch (JsonProcessingException ex) {
            log.error("Error serializing TodoRequest: {}", ex.getMessage(), ex);
        }
    }
}