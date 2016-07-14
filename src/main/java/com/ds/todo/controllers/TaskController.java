package com.ds.todo.controllers;

import com.ds.todo.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Route;

import javax.sql.DataSource;

import java.util.List;

import static spark.Spark.*;
/**
 * Created by dsutedja on 6/28/16.
 */
public class TaskController {

    private DataSource dataSource;
    private AuthenticationController authController;
    private UserRepository userRepository;
    private UserSessionRepository sessionRepository;
    private TaskRepository taskRepository;

    public TaskController(DataSource dataSource, AuthenticationController authController) {
        this.dataSource = dataSource;
        this.authController = authController;

        userRepository = new UserRepository(dataSource);
        sessionRepository = new UserSessionRepository(dataSource);
        taskRepository = new TaskRepository(dataSource);

        get("/apis/:version/todos/", listTodos());
        get("/apis/:version/todos/:id", getSingleTodos());
        get("/apis/:version/todos/:id/:status", listTodosByStatus());
        get("/apis/:version/todos/range/:start_time/:end_time", listTodosWithTimeRange());
        get("/apis/:version/todos/title/:prefix", listTodosNamesByPrefix());
        get("/apis/:version/todos/title/:prefix/:status", listTodosNamesByStatusAndPrefix());
        get("/apis/:version/todos/title/:prefix/:status/:start_time/:end_time", listTodosNamesByPrefixStatusTimeRange());

        post("/apis/:version/todos/", createNewTodos());

        put("/apis/:version/todos/:id", updateSingleTodos());

        delete("/apis/:version/todos/:id", deleteSingleTodos());
        delete("/apis/:version/todos_withstatus/:status", deleteTodosByStatus());
    }

    private Route listTodos() {
        return (req, res) -> {
            String sessionID = req.queryParams(AuthenticationController.KEY_SESSION_ID);
            UserSession session = sessionRepository.findBySessionID(sessionID);
            if (session != null) {
                int userID = session.getUserID();
                List<Task> tasks = taskRepository.findByUserId(userID);
                String json = "";
                if (tasks != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    json = mapper.writeValueAsString(tasks);
                }
                return json;
            } else {
                return "";
            }
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

    private Route getSingleTodos() {
        return (req, res) -> {
            String sessionID = req.queryParams(AuthenticationController.KEY_SESSION_ID);
            UserSession session = sessionRepository.findBySessionID(sessionID);
            if (session != null) {
                String taskID = req.params(":id");
                String json = "";
                try {
                    Task task = taskRepository.findByTaskID(Integer.parseInt(taskID));
                    if (task != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        json = mapper.writeValueAsString(task);
                    }
                } catch (NumberFormatException er) {
                    json = "{ error:InvalidTaskID }";
                }
                return json;
            } else {
                return "";
            }
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
