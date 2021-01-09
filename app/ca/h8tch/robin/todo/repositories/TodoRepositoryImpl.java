package ca.h8tch.robin.todo.repositories;

import ca.h8tch.robin.db.ExecutionContext;
import ca.h8tch.robin.db.models.Todo;
import lombok.extern.slf4j.Slf4j;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Singleton
public class TodoRepositoryImpl implements TodoRepository {

    private final JPAApi jpa;
    private final ExecutionContext ec;

    @Inject
    public TodoRepositoryImpl(JPAApi api, ExecutionContext ec) {
        this.jpa = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Todo> add(Todo todo) {
        return supplyAsync(() -> wrap(em -> persist(em, todo)), ec);
    }

    @Override
    public CompletionStage<Todo> update(Todo todo) {
        return supplyAsync(() -> wrap(em -> merge(em, todo)), ec);
    }

    @Override
    public CompletionStage<Optional<Todo>> get(UUID id) {
        return supplyAsync(() -> wrap(em -> find(em, id)), ec);
    }

    @Override
    public CompletionStage<List<Todo>> list(Integer page, Integer size, boolean done) {
        return supplyAsync(() -> wrap(em -> findAll(em, page, size, done)), ec);
    }

    @Override
    public CompletionStage<Todo> toggle(UUID id) {
        return supplyAsync(() -> wrap(em -> toggleStatus(em, id)), ec);
    }

    @Override
    public CompletionStage<Integer> deleteAll() {
        return supplyAsync(() -> wrap(em -> removeAll(em)), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpa.withTransaction(function);
    }

    private Todo persist(EntityManager em, Todo todo) {
        em.persist(todo);
        return todo;
    }

    private Todo merge(EntityManager em, Todo todo) {
        return em.merge(todo);
    }

    private Optional<Todo> find(EntityManager em, UUID id) {
        return Optional.ofNullable(em.find(Todo.class, id));
    }

    private int removeAll(EntityManager em) {
        Query query = em.createQuery("DELETE from Todo");
        return query.executeUpdate();
    }

    private List<Todo> findAll(EntityManager em, Integer page, Integer size, boolean done) {
        TypedQuery<Todo> query = em.createQuery(
            "SELECT t from Todo t WHERE t.completed = :completed ORDER BY t.modified DESC", Todo.class
        )
            .setParameter("completed", done);
        if (done) {
            query = query.setMaxResults(10);
        } else if (page != null && size != null) {
            query = query.setFirstResult((page - 1) * size)
                .setMaxResults(size);
        }
        return query.getResultList();
    }

    private Todo toggleStatus(EntityManager em, UUID id) {
        Query query = em.createQuery(
            "UPDATE Todo t SET t.modified = :modified, t.completed = CASE t.completed WHEN TRUE THEN FALSE ELSE TRUE END " +
                "WHERE t.id = :id"
        )
            .setParameter("id", id)
            .setParameter("modified", LocalDateTime.now());
        query.executeUpdate();
        return em.find(Todo.class, id);
    }
}
