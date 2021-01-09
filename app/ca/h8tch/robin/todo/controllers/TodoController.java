package ca.h8tch.robin.todo.controllers;

import ca.h8tch.robin.common.CommandExecutionException;
import ca.h8tch.robin.todo.commands.AddTodoCommand;
import ca.h8tch.robin.todo.commands.DeleteAllTodoCommand;
import ca.h8tch.robin.todo.commands.ListTodoCommand;
import ca.h8tch.robin.todo.commands.UpdateTodoCommand;
import ca.h8tch.robin.todo.models.TodoDTO;
import ca.h8tch.robin.todo.requests.TodoRequest;
import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class TodoController extends Controller {

    private final AddTodoCommand addCommand;
    private final DeleteAllTodoCommand deleteCommand;
    private final ListTodoCommand listCommand;
    private final UpdateTodoCommand updateCommand;

    @Inject
    public TodoController(AddTodoCommand add, DeleteAllTodoCommand del, ListTodoCommand list, UpdateTodoCommand update) {
        this.addCommand = add;
        this.deleteCommand = del;
        this.listCommand = list;
        this.updateCommand = update;
    }

    public CompletionStage<Result> add(Request httpRequest) {
        try {
            return addCommand.execute(TodoRequest.builder()
                .dto(buildDTO(httpRequest))
                .build()
            ).thenApplyAsync(data -> {
                return created(Json.toJson(data))
                    .withHeader("Access-Control-Allow-Origin", "*");
            });
        } catch (CommandExecutionException e) {
            return CompletableFuture.supplyAsync(() -> internalServerError(e.getMessage()));
        }
    }

    public CompletionStage<Result> clearAll() {
        try {
            return deleteCommand.execute(TodoRequest.builder()
                .build()
            ).thenApplyAsync(data -> {
                return ok(Json.toJson(data))
                    .withHeader("Access-Control-Allow-Origin", "*");

            });
        } catch (CommandExecutionException e) {
            return CompletableFuture.supplyAsync(() -> internalServerError(e.getMessage()));
        }
    }

    public CompletionStage<Result> list(Integer page, Integer size, boolean done) {
        try {
            return listCommand.execute(TodoRequest.builder()
                .page(page)
                .size(size)
                .done(done)
                .build()
            ).thenApplyAsync(data -> {
                return ok(Json.toJson(data))
                    .withHeader("Access-Control-Allow-Origin", "*");
            });
        } catch (CommandExecutionException e) {
            return CompletableFuture.supplyAsync(() -> internalServerError(e.getMessage()));
        }
    }

    public CompletionStage<Result> toggle(UUID id) {
        try {
            return updateCommand.execute(TodoRequest.builder()
                .dto(TodoDTO.builder().id(id).build())
                .build()
            ).thenApplyAsync(data -> {
                return ok(Json.toJson(data))
                    .withHeader("Access-Control-Allow-Origin", "*");
            });
        } catch (CommandExecutionException e) {
            return CompletableFuture.supplyAsync(() -> internalServerError(e.getMessage()));
        }
    }

    public CompletionStage<Result> optionsWithId(UUID id) {
        return options();
    }

    public CompletionStage<Result> options() {
        return CompletableFuture.supplyAsync(() ->
            ok("")
                .withHeader("Access-Control-Allow-Origin", "*")
                .withHeader("Access-Control-Allow-Headers", "*")
                .withHeader("Access-Control-Allow-Methods", "*")
        );
    }


    protected TodoDTO buildDTO(Request httpRequest) {
        return Json.fromJson(httpRequest.body().asJson(), TodoDTO.class);
    }
}
