package com.example.todoappserver.adapter.out.persistence;

import com.example.todoappserver.domainold.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Modifying
    @Query("delete from Todo t where t.isCompleted = true")
    int deleteCompletedTodo();
}
