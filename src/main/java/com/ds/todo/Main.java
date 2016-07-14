package com.ds.todo;

import com.ds.todo.controllers.AuthenticationController;
import com.ds.todo.controllers.TaskController;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by dsutedja on 6/20/16.
 */
public class Main {
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig("hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    public static void main(String[] argv) {
        AuthenticationController auth = new AuthenticationController(dataSource);
        new TaskController(dataSource, auth);
    }
}
