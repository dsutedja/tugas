package com.ds.todo.controllers;

import com.ds.todo.models.*;
import com.ds.todo.utils.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Request;
import spark.Response;

import javax.sql.DataSource;

/**
 * Created by esutedja on 7/10/16.
 */
public class TestTaskController {
    private Request mockRequest;
    private Response mockResponse;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private UserSessionRepository sessionRepository;

    private static DataSource dataSource;
    private User fakeUser;

    static {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
    }

    @Before
    public void initialize() {
        // let's clear the test DB clean now
        Sql2o engine = new Sql2o(dataSource);
        try (Connection conn = engine.open()) {
            String sql = "delete from User";
            conn.createQuery(sql).executeUpdate();
            sql = "delete from Task";
            conn.createQuery(sql).executeUpdate();
            sql = "delete from UserSession";
            conn.createQuery(sql).executeUpdate();
        } catch (Exception er) {
            er.printStackTrace();
        }

        userRepository = new UserRepository(dataSource);
        fakeUser = getFakeUser();
        userRepository.insert(fakeUser);

        taskRepository = new TaskRepository(dataSource);

        mockRequest = Mockito.mock(Request.class);
        Mockito.when(mockRequest.queryParams(AuthenticationController.KEY_SESSION_ID)).thenReturn("foobar");

        mockResponse = Mockito.mock(Response.class);
    }

    @Test
    public void testListTodos() {

    }

    private Task getFakeTask() {
        Task task = new Task();
        task.setState(Task.State.COMPLETED);
        task.setDescription("Test Task Description");
        task.setId(-1);
        task.setUserId(fakeUser.getId());
        task.setLastModified(System.currentTimeMillis());
        task.setTitle("Test Task Title");
        return task;
    }

    private User getFakeUser() {
        User user = new User();
        user.setUsername("dsfx");
        String salt = "smartphone";
        String password = "1HeadPhone";
        String encoded = PasswordUtil.md5(password, salt);
        user.setPassword(encoded);
        user.setSalt(salt);
        user.setCreationTime(System.currentTimeMillis());
        user.setLastMod(System.currentTimeMillis());
        return user;
    }
}
