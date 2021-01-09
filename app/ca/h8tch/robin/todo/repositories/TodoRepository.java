package ca.h8tch.robin.todo.repositories;

import ca.h8tch.robin.db.models.Todo;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@ImplementedBy(TodoRepositoryImpl.class)
public interface TodoRepository {

    CompletionStage<Todo> add(Todo todo);
    CompletionStage<Todo> update(Todo todo);
    CompletionStage<Optional<Todo>> get(UUID id);
    CompletionStage<Todo> toggle(UUID id);
    CompletionStage<List<Todo>> list(Integer page, Integer size, boolean done);
    CompletionStage<Integer> deleteAll();
}
