package ca.h8tch.robin.todo.controllers;

import ca.h8tch.robin.db.models.Todo;
import ca.h8tch.robin.todo.models.TodoDTO;
import ca.h8tch.robin.todo.repositories.TodoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.DELETE;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

public class TodoControllerTest extends WithApplication {

    private TodoDTO testDTO;
    private TodoRepository repository;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setUp() throws Exception {
        repository = app.injector().instanceOf(TodoRepository.class);
        testDTO = TodoDTO.builder()
            .id(UUID.randomUUID())
            .task("TEST")
            .completed(false)
            .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddTodo() {

        Result result = addTodo();
        Todo saved = Json.fromJson(Json.parse(contentAsString(result)), Todo.class);

        assertThat("HttpStatus Should Be CREATED (201)", result.status(), is(CREATED));
        assertThat("Task Should Be Equal", saved.getTask(), is(testDTO.getTask()));
        assertThat("Completed Should Be Equal", saved.isCompleted(), is(testDTO.isCompleted()));
        assertThat("ID Should Not Be Null", saved.getId(), notNullValue());
    }

    @Test
    public void testClearAllOnEmptyReturnsZero() {

        Result result = clearTodos();
        Map response = Json.fromJson(Json.parse(contentAsString(result)), Map.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("Deleted Should Be Zero", response.get("deleted"), is(0));
    }

    @Test
    public void testClearAllOnNonEmptyReturnsNonZero() {

        addTodo();

        Result result = clearTodos();
        Map response = Json.fromJson(Json.parse(contentAsString(result)), Map.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("Deleted Should Not Be Zero", response.get("deleted"), is(1));
    }

    @Test
    public void testListDoneOnEmptyShouldReturnEmptyList() {

        Result result = listDoneTodos();
        List response = Json.fromJson(Json.parse(contentAsString(result)), List.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("List Should Be Empty", response.size(), is(0));
    }

    @Test
    public void testListNotDoneOnEmptyShouldReturnEmptyList() {

        Result result = listNotDoneTodos();
        List response = Json.fromJson(Json.parse(contentAsString(result)), List.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("List Should Be Empty", response.size(), is(0));
    }

    @Test
    public void testListNotDoneOnNonEmptyShouldReturnNotEmptyNotDoneList() {

        addTodo(); // will add a not done Todo
        Result result = listNotDoneTodos();
        List response = Json.fromJson(Json.parse(contentAsString(result)), List.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("List Should Be Not Empty", response.size(), is(1));
    }

    @Test
    public void testListNotDoneOnNonEmptyShouldReturnEmptyDoneList() {

        addTodo(); // will add a not done Todo
        Result result = listDoneTodos();
        List response = Json.fromJson(Json.parse(contentAsString(result)), List.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("List Should Be Empty", response.size(), is(0));
    }


    @Test
    public void testToggleChangesState() {

        Result result = addTodo(); // will add a not done Todo
        Todo saved = Json.fromJson(Json.parse(contentAsString(result)), Todo.class);

        result = toggleTodo(saved.getId());
        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));

        result = listDoneTodos();
        List response = Json.fromJson(Json.parse(contentAsString(result)), List.class);

        assertThat("HttpStatus Should Be OK (200)", result.status(), is(OK));
        assertThat("List Should Not Be Empty", response.size(), is(1));
    }

    protected Result addTodo() {
        testDTO.setId(null);
        Http.RequestBuilder request = new Http.RequestBuilder()
            .method(POST)
            .bodyJson(Json.toJson(testDTO))
            .uri("/v1/todos");
        return route(app, request);
    }

    protected Result clearTodos() {
        Http.RequestBuilder request = new Http.RequestBuilder()
            .method(DELETE)
            .uri("/v1/todos");
        return route(app, request);
    }

    protected Result listNotDoneTodos() {
        Http.RequestBuilder request = new Http.RequestBuilder()
            .method(GET)
            .uri("/v1/todos?done=false");
        return route(app, request);
    }

    protected Result listDoneTodos() {
        Http.RequestBuilder request = new Http.RequestBuilder()
            .method(GET)
            .uri("/v1/todos?done=true");
        return route(app, request);
    }

    protected Result toggleTodo(UUID id) {
        Http.RequestBuilder request = new Http.RequestBuilder()
            .method(PUT)
            .uri("/v1/todos/" + id);
        return route(app, request);
    }
}
