# Installation & Launch

To launch, clone this repo and run "sbt run" from your command line.
If you don't have SBT installed, there is a distributed sbt in this project.
Simply use "./sbt run" instead.

This should run the server on your localhost at port 9000.

The following routes are available:

* list all the todos that are done:  GET http://localhost:9000/v1/todos?done=true
* list all the todos that are not done:  GET http://localhost:9000/v1/todos?done=false

* toggle the done state of the todos: PUT http://localhost:9000/v1/todos/:id

* create a new todo:  POST http://localhost:9000/v1/todos - a simple json body is { "task": "", "completed": false }

* clear all the todos:  DELETE http://localhost:9000/v1/todos



