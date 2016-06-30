package com.ds.todo.models;

import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TaskRepository {
    private Sql2o mSql;

    public TaskRepository(DataSource dataSource) {
        mSql = new Sql2o(dataSource);
    }

    public Task findByTaskID(int id) {
        return null;
    }

    public List<Task> findByUserId(int userID) {
        return null;
    }

    public List<Task> findByUserIdAndState(int userId, Task.State state) {
        return null;
    }

    public boolean insert(Task task) {
        return false;
    }

    public boolean update(Task task) {
        return false;
    }

    public boolean delete(Task task) {
        return false;
    }
}


