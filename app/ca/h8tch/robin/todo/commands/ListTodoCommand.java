package ca.h8tch.robin.todo.commands;

import ca.h8tch.robin.common.Command;
import ca.h8tch.robin.common.CommandExecutionException;
import ca.h8tch.robin.todo.models.TodoDTO;
import ca.h8tch.robin.todo.repositories.TodoRepository;
import ca.h8tch.robin.todo.requests.TodoRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ListTodoCommand implements Command<TodoRequest, CompletionStage<List<TodoDTO>>> {

    private final TodoRepository repository;

    @Inject
    public ListTodoCommand(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletionStage<List<TodoDTO>> execute(TodoRequest request) throws CommandExecutionException {
        return repository.list(request.getPage(), request.getSize(), request.isDone()).thenApplyAsync(resourceList -> {
            log.debug("listed resource: {}", resourceList);
            return resourceList.stream()
                .map(TodoDTO::from)
                .collect(Collectors.toList()
                );
        });
    }
}

