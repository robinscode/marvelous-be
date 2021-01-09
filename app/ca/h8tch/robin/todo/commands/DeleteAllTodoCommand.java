package ca.h8tch.robin.todo.commands;

import ca.h8tch.robin.common.Command;
import ca.h8tch.robin.common.CommandExecutionException;
import ca.h8tch.robin.todo.repositories.TodoRepository;
import ca.h8tch.robin.todo.requests.TodoRequest;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@Data
@Slf4j
public class DeleteAllTodoCommand implements Command<TodoRequest, CompletionStage<Map<String, Integer>>> {

    private final TodoRepository repository;

    @Inject
    public DeleteAllTodoCommand(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletionStage<Map<String, Integer>> execute(TodoRequest request) throws CommandExecutionException {
        return repository.deleteAll().thenApplyAsync(result -> {
            log.debug("deleted {} records", result);
            return ImmutableMap.of("deleted", result);
        });
    }
}
