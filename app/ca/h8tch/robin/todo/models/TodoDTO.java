package ca.h8tch.robin.todo.models;


import ca.h8tch.robin.db.models.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private UUID id;
    private String task;

    @Builder.Default
    private boolean completed = false;

    private LocalDateTime modified;

    public static TodoDTO from(Todo todo) {
        return TodoDTO.builder()
            .id(todo.getId())
            .task(todo.getTask())
            .completed(todo.isCompleted())
            .modified(todo.getModified())
            .build();
    }
}
