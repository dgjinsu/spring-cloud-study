package todo_app_server.domain.todo.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import todo_app_server.domain.todo.dto.TodoResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final Map<List<Long>, CompletableFuture<List<TodoResponse>>> responseFutures = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public CompletableFuture<List<TodoResponse>> listenForBulkCreatedAtInfo(List<Long> todoIds) {
        CompletableFuture<List<TodoResponse>> future = new CompletableFuture<>();
        responseFutures.put(todoIds, future);
        return future;
    }

    @KafkaListener(topics = "todo-response-topic", groupId = "todo-group")
    public void handleBulkResponse(List<TodoResponse> todoResponses) {
        try {
            // 응답의 todoIds를 추출하여 해당 CompletableFuture를 찾아 완료
            List<Long> receivedTodoIds = todoResponses.stream()
                    .map(TodoResponse::todoId)
                    .collect(Collectors.toList());

            CompletableFuture<List<TodoResponse>> future = responseFutures.remove(receivedTodoIds);
            if (future != null) {
                future.complete(todoResponses);
            } else {
                log.warn("No future found for todoIds {}", receivedTodoIds);
            }
        } catch (Exception e) {
            log.error("Error processing Kafka response: {}", e.getMessage(), e);
        }
    }
}