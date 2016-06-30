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
    public static void main(String[] argv) {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        props.setProperty("dataSource.user", "dsutedja");
        props.setProperty("dataSource.password", "__Test12");
        props.setProperty("dataSource.databaseName", "ds_todos_1");
        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        DataSource dataSource = new HikariDataSource(config);
        new AuthenticationController(dataSource);
    }
}
