package todo_app_server.domain.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo_app_server.domain.todo.dto.TodoUpdateRequest;
import todo_app_server.domain.todo.dto.TodoSaveRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo {

    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Builder
    public Todo(String content, boolean isCompleted) {
        this.content = content;
        this.isCompleted = isCompleted;
    }

    public void updateTodo(TodoUpdateRequest request) {
        if (request.content() != null) {
            this.content = request.content();
        }

        if (request.isComplete() != null) {
            this.isCompleted = request.isComplete();
        }
    }
}
