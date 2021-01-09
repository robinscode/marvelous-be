package ca.h8tch.robin.todo.requests;

import ca.h8tch.robin.common.CommandRequest;
import ca.h8tch.robin.todo.models.TodoDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoRequest implements CommandRequest<TodoDTO> {

    TodoDTO dto;

    @Builder.Default
    Integer page = null;

    @Builder.Default
    Integer size = null;

    @Builder.Default
    boolean done = false;
}
