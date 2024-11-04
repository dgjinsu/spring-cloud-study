package todo_app_server.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import todo_app_server.domain.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Modifying
    @Query("delete from Todo t where t.isCompleted = true")
    int deleteCompletedTodo();
}
