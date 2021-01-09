package ca.h8tch.robin.todo.commands;

import ca.h8tch.robin.common.Command;
import ca.h8tch.robin.common.CommandExecutionException;
import ca.h8tch.robin.db.models.Todo;
import ca.h8tch.robin.todo.models.TodoDTO;
import ca.h8tch.robin.todo.repositories.TodoRepository;
import ca.h8tch.robin.todo.requests.TodoRequest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Data
@Slf4j
public class AddTodoCommand implements Command<TodoRequest, CompletionStage<TodoDTO>> {

    private final TodoRepository repository;

    @Inject
    public AddTodoCommand(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletionStage<TodoDTO> execute(TodoRequest request) throws CommandExecutionException {
        TodoDTO dto = request.getDto();
        Todo data = Todo.builder()
            .task(Optional.ofNullable(dto.getTask()).orElse(""))
            .completed(Optional.ofNullable(dto.isCompleted()).orElse(false))
            .build();

        return repository.add(data).thenApplyAsync(resource -> {
            log.debug("saved resource: {}", resource);
            return TodoDTO.from(resource);
        });
    }
}
