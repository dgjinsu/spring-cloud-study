package temp_server.domain.temp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import temp_server.domain.temp.entity.Temp;
import temp_server.domain.temp.message.dto.TodoResponse;
import temp_server.domain.temp.message.dto.TodoSaveRequest;
import temp_server.domain.temp.repository.TempRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TempService {
    private final TempRepository tempRepository;

    public void saveTemp(TodoSaveRequest request) {
        Temp temp = Temp.builder().content(request.content()).createdAt(LocalDateTime.now()).todoId(request.todoId()).build();

        tempRepository.save(temp);
    }

    public Map<Long, LocalDateTime> getCreatedAtByTodoIds(List<Long> todoIds) {
        List<Temp> temps = tempRepository.findByTodoIds(todoIds);
        return temps.stream()
                .collect(Collectors.toMap(Temp::getTodoId, Temp::getCreatedAt));
    }
}
