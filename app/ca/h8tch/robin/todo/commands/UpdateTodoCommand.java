package ca.h8tch.robin.todo.commands;

import ca.h8tch.robin.common.Command;
import ca.h8tch.robin.common.CommandExecutionException;
import ca.h8tch.robin.todo.models.TodoDTO;
import ca.h8tch.robin.todo.repositories.TodoRepository;
import ca.h8tch.robin.todo.requests.TodoRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Data
@Slf4j
public class UpdateTodoCommand implements Command<TodoRequest, CompletionStage<TodoDTO>> {

    private final TodoRepository repository;

    @Inject
    public UpdateTodoCommand(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletionStage<TodoDTO> execute(TodoRequest request) throws CommandExecutionException {
        UUID id = Optional.ofNullable(request.getDto())
            .map(TodoDTO::getId)
            .orElseThrow(() -> new CommandExecutionException("ID needed"));
        return repository.toggle(id).thenApplyAsync(resource -> TodoDTO.from(resource));
    }
}
