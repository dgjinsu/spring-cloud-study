package temp_server.domain.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import temp_server.domain.temp.entity.Temp;

import java.util.List;

public interface TempRepository extends JpaRepository<Temp, Long> {
    Temp findByTodoId(Long todoId);

    @Query("select t from Temp t where t.todoId in :todoIds")
    List<Temp> findByTodoIds(List<Long> todoIds);
}
