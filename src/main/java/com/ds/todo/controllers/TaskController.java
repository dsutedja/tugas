package com.ds.todo.controllers;

import spark.Route;

import javax.activation.DataSource;

import static spark.Spark.*;
/**
 * Created by dsutedja on 6/28/16.
 */
public class TaskController {

    private DataSource dataSource;
    private AuthenticationController authController;

    public TaskController(DataSource dataSource, AuthenticationController authController) {
        this.dataSource = dataSource;
        this.authController = authController;

        get("/apis/:version/todos/", listTodos());
        get("/apis/:version/todos/:id", getSingleTodos());
        get("/apis/:version/todos/:start_time/:end_time", listTodosWithTimeRange());
        get("/apis/:version/todos/find/:id", getSingleTodosByID());
        get("/apis/:version/todos_withstatus/find/:status", listTodosByStatus());
        get("/apis/:version/todos_titles/:prefix", listTodosNamesByPrefix());
        get("/apis/:version/todos_titles/:prefix/:status", listTodosNamesByStatusAndPrefix());
        get("/apis/:version/todos_titles/:prefix/:status/:start_time/:end_time", listTodosNamesByPrefixStatusTimeRange());

        post("/apis/:version/todos/", createNewTodos());

        put("/apis/:version/todos/:id", updateSingleTodos());

        delete("/apis/:version/todos/:id", deleteSingleTodos());
        delete("/apis/:version/todos_withstatus/:status", deleteTodosByStatus());
    }

    private Route listTodos() {
        return (req, res) -> {
            return "";
        };
    }

    private Route listTodosNamesByPrefixStatusTimeRange() {
        return (req, res) -> {
            return "";
        };
    }

    private Route listTodosNamesByPrefix() {
        return (req, res) -> {
            return "";
        };
    }

    // This will return ONLY the names of the todos matching the provided status and prefix
    private Route listTodosNamesByStatusAndPrefix() {
        return (req, res) -> {
            return "";
        };
    }

    private Route listTodosWithTimeRange() {
        return (req, res) -> {
            return "";
        };
    }

    private Route listTodosByStatus() {
        return (req, res) -> {
            return "";
        };
    }

    private Route getSingleTodosByID() {
        return (req, res) -> {
            return "";
        };
    }

    private Route getSingleTodos() {
        return (req, res) -> {
            return "";
        };
    }

    private Route createNewTodos() {
        return (req, res) -> {
            return "";
        };
    }

    private Route updateSingleTodos() {
        return (req, res) -> {
            return "";
        };
    }

    private Route deleteSingleTodos() {
        return (req, res) -> {
            return "";
        };
    }

    private Route deleteTodosByStatus() {
        return (req, res) -> {
            return "";
        };
    }
}
