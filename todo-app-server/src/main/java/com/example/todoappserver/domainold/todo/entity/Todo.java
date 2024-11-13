package com.example.todoappserver.domainold.todo.entity;

import com.example.todoappserver.domainold.todo.dto.TodoUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "member_id")
    private Long memberId;

    @Builder
    public Todo(String content, boolean isCompleted, Long memberId) {
        this.content = content;
        this.isCompleted = isCompleted;
        this.memberId = memberId;
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
