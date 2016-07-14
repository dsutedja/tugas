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

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATE = "state";

    private static class TaskFormData {
        private String title;
        private String description;
        private int state;

        public TaskFormData() {

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

    }

    public TaskController(DataSource dataSource, AuthenticationController authController) {
        this.dataSource = dataSource;
        this.authController = authController;

        userRepository = new UserRepository(dataSource);
        sessionRepository = new UserSessionRepository(dataSource);
        taskRepository = new TaskRepository(dataSource);

//        // make sure client is authenticated
//        before("/apis/:version/todos/*", (req, res) -> {
//            String sessionID = req.queryParams(AuthenticationController.KEY_SESSION_ID);
//            UserSession session = null;
//            if (sessionID != null && !sessionID.isEmpty()) {
//                session = sessionRepository.findBySessionID(sessionID);
//            }
//            if (session == null) {
//                halt(401, "You need to login!");
//            }
//        });

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

    private UserSession findUserSession(String sessionId) {
        UserSession session = null;
        if (sessionId != null && !sessionId.isEmpty()) {
            session = sessionRepository.findBySessionID(sessionId);
        }
        return session;
    }

    private Route listTodos() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {
                int userID = session.getUserID();
                List<Task> tasks = taskRepository.findByUserId(userID);
                if (tasks != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    retVal = mapper.writeValueAsString(tasks);
                }
            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route listTodosNamesByPrefixStatusTimeRange() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route listTodosNamesByPrefix() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    // This will return ONLY the names of the todos matching the provided status and prefix
    private Route listTodosNamesByStatusAndPrefix() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route listTodosWithTimeRange() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route listTodosByStatus() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route getSingleTodos() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {
                String taskID = req.params(":id");
                try {
                    Task task = taskRepository.findByTaskID(Integer.parseInt(taskID));
                    if (task != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        retVal = mapper.writeValueAsString(task);
                    }
                } catch (NumberFormatException er) {
                    retVal  = "{ error:InvalidTaskID }";
                }
            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route createNewTodos() {
        return (req, res) -> {
            String retVal = "";
            String sessionID = req.queryParams(AuthenticationController.KEY_SESSION_ID);
            UserSession session = sessionRepository.findBySessionID(sessionID);
            if (session != null) {
                String jFormData = req.body();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    TaskFormData formData = mapper.readValue(jFormData, TaskFormData.class);

                    int iState = 0;
                    try {
                        iState = formData.state;
                        if (iState < Task.State.NONE.ordinal() && iState > Task.State.COMPLETED.ordinal()) {
                            iState = 0;
                        }
                    } catch (Exception er) {
                        // noop
                    }
                    // now let's create the new task
                    Task task = new Task();
                    task.setUserId(session.getUserID());
                    task.setCreationTime(System.currentTimeMillis());
                    task.setTitle(formData.title);
                    task.setDescription(formData.description);
                    task.setState(Task.State.getState(iState));
                    task.setLastModified(System.currentTimeMillis());

                    taskRepository.insert(task);
                    retVal = "Task created";
                } catch (Exception er) {
                    retVal = "ERROR: invalid data received";
                }
            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route updateSingleTodos() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route deleteSingleTodos() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }

    private Route deleteTodosByStatus() {
        return (req, res) -> {
            String retVal = "";
            UserSession session = findUserSession(req.queryParams(AuthenticationController.KEY_SESSION_ID));
            if (session != null) {

            } else {
                halt(401, "You need to login!");
            }
            return retVal;
        };
    }
}
