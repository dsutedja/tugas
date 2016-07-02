package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.IDGenerator;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TaskRepository {
    private Sql2o mSql;
    private static final int THRESHOLD = 10;

    public TaskRepository(DataSource dataSource) {
        mSql = new Sql2o(dataSource);
    }

    // --- TEST CODE
    void deleteAll() {
        String sql = "DELETE from TASK";
        try (Connection conn = mSql.open()) {
            conn.createQuery(sql).executeUpdate();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }
    // --- END TEST CODE


    public Task findByTaskID(int id) {
        Task retVal = null;

        String sql = "SELECT id, user_id, title, description, state, DATE_FORMAT(lastmod, '%Y-%m-%d %H:%i:%s') as lastmod FROM TASK WHERE id = :id";

        try (Connection conn = mSql.open()) {
            List<Task> results = conn.createQuery(sql)
                    .addParameter("id", id)
                    .addColumnMapping("user_id", "userId")
                    .addColumnMapping("lastmod", "lastModified")
                    .executeAndFetch(Task.class);
            if (results.size() != 1) {
                System.out.println("ERR: invalid return size for tasks");
            } else {
                retVal = results.get(0);
            }
        } catch (Exception er) {
            er.printStackTrace();
        }

        return retVal;
    }

    public List<Task> findByUserId(int userID) {
        List<Task> results = null;

        String sql = "SELECT id, user_id, title, description, state, DATE_FORMAT(lastmod, '%Y-%m-%d %H:%i:%s') as lastmod FROM TASK where user_id = :user_id";

        try (Connection conn = mSql.open()) {
            results = conn.createQuery(sql)
                    .addParameter("user_id", userID)
                    .addColumnMapping("user_id", "userId")
                    .addColumnMapping("lastmod", "lastModified")
                    .executeAndFetch(Task.class);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return results;
    }

    public List<Task> findByUserIdAndState(int userId, Task.State state) {
        List<Task> results = null;

        String sql = "SELECT id, user_id, title, description, state, DATE_FORMAT(lastmod, '%Y-%m-%d %H:%i:%s') as lastmod FROM TASK where user_id = :user_id and state = :state";

        try (Connection conn = mSql.open()) {
            results = conn.createQuery(sql)
                    .addParameter("user_id", userId)
                    .addParameter("state", state.ordinal())
                    .addColumnMapping("user_id", "userId")
                    .addColumnMapping("lastmod", "lastModified")
                    .executeAndFetch(Task.class);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return results;
    }

    public Task insert(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Cannot insert null task");
        }

        String sql =
                "INSERT INTO TASK (id, user_id, title, description, state, lastmod)"
                + " values (:id, :user_id, :title, :description, :state, :lastmod)";

        int count = 0;
        try (Connection conn = mSql.open()) {
            int retry = 0;
            while (count == 0 && retry <= THRESHOLD) {
                retry ++;
                int id = IDGenerator.nextID();
                count = conn.createQuery(sql)
                        .addParameter("id", id)
                        .addParameter("user_id", task.getUserId())
                        .addParameter("title", task.getTitle())
                        .addParameter("description", task.getDescription())
                        .addParameter("state", task.getState().ordinal())
                        .addParameter("lastmod", task.getLastModified())
                        .executeUpdate()
                        .getResult();
                if (count > 0) {
                    task.setId(id);
                }
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
        return count > 0 ? task : null;
    }

    public Task update(Task task) {
        if (task == null || task.getId() == -1) {
            throw new IllegalArgumentException("Invalid task to update");
        }

        String sql = "UPDATE TASK SET"
                + " title = :title, "
                + " description = :description, "
                + " state = :state, "
                + " lastmod = :lastmod"
                + " WHERE id = :id and user_id = :user_id";
        int count = 0;
        try (Connection conn = mSql.open()) {
            count = conn.createQuery(sql)
                    .addParameter("title", task.getTitle())
                    .addParameter("description", task.getDescription())
                    .addParameter("state", task.getState().ordinal())
                    .addParameter("lastmod", DatesUtil.nowToSQLTimestamp())
                    .addParameter("id", task.getId())
                    .addParameter("user_id", task.getUserId())
                    .executeUpdate()
                    .getResult();
        } catch (Exception er) {
            er.printStackTrace();
        }

        return count == 1 ? task : null;
    }

    public boolean delete(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Cannot delete null task");
        }

        String sql = "DELETE FROM TASK WHERE id = :id and user_id = :user_id";

        int count = 0;
        try (Connection conn = mSql.open()) {
            count = conn.createQuery(sql)
                    .addParameter("id", task.getId())
                    .addParameter("user_id", task.getUserId())
                    .executeUpdate()
                    .getResult();
        } catch (Exception er) {
            er.printStackTrace();
        }

        return count == 1;
    }
}


