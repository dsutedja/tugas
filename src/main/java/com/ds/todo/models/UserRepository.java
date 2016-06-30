package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.IDGenerator;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by dsutedja on 6/21/16.
 */
public class UserRepository {
    private static final int RETRY_THRESHOLD = 10;
    private Sql2o mSql;

    public UserRepository(DataSource dataSource) {
        mSql = new Sql2o(dataSource);
    }

    //---- TESTING CODE ----//

    void deleteAll() {
        String sql = "DELETE from USER";
        try (Connection conn = mSql.open()) {
            conn.createQuery(sql).executeUpdate();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }
    //----- TESTING CODE ---//

    public User findByUsername(String username) {
        String sql =
                "SELECT * FROM USER where username = :username";
        User retVal = null;
        try (Connection conn = mSql.open()) {
            List<User> results = conn.createQuery(sql)
                    .addParameter("username", username)
                    .addColumnMapping("creation_date", "creationDate")
                    .executeAndFetch(User.class);
            if (results != null && !results.isEmpty()) {
                retVal = results.get(0);
                retVal.mLoadedFromDB = true;
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        return retVal;
    }

    public User findByUserID(int userID) {
        String sql =
                "SELECT * FROM USER where id = :id";
        User retVal = null;
        try (Connection conn = mSql.open()) {
            List<User> results = conn.createQuery(sql)
                    .addParameter("id", userID)
                    .addColumnMapping("creation_date", "creationDate")
                    .executeAndFetch(User.class);

            if (results != null && !results.isEmpty()) {
                retVal = results.get(0);
                retVal.mLoadedFromDB = true;
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
        return retVal;
    }

    public User insert(User user) {
        String sql =
                "INSERT INTO USER (id, username, password, salt, creation_date, lastmod)"
                + " values (:id, :username, :password, :salt, :creation_date, :lastmod)";

        int count = 0;
        int retry = 0;
        long start = System.currentTimeMillis();
        try (Connection conn = mSql.open()) {
            System.out.println("** insert's open connection: elapsed: " + (System.currentTimeMillis() - start) + " ms");
            while (count == 0 && retry <= RETRY_THRESHOLD) {
                retry ++;
                int randomID = IDGenerator.nextID();
                start = System.currentTimeMillis();
                count = conn.createQuery(sql)
                        .addParameter("id", randomID)
                        .addParameter("username", user.getUsername())
                        .addParameter("password", user.getPassword())
                        .addParameter("salt", user.getSalt())
                        .addParameter("creation_date", user.getCreationDate())
                        .addParameter("lastmod", user.getLastMod())
                        .executeUpdate()
                        .getResult();
                System.out.println("** insert's execution: elapsed: " + (System.currentTimeMillis() - start) + " ms");
                user.setId(randomID);
            }
        } catch (Exception er) {
            er.printStackTrace();
        }

        return count > 0 ? user : null;
    }

    public boolean update(User user) {
        String sql =
                "UPDATE USER"
                + " SET username = :username, password = :password, salt = :salt, lastmod = :lastmod"
                + " WHERE id = :id";

        int count = 0;

        try (Connection conn = mSql.open()) {
            count = conn.createQuery(sql)
                    .addParameter("username", user.getUsername())
                    .addParameter("password", user.getPassword())
                    .addParameter("salt", user.getSalt())
                    .addParameter("lastmod", DatesUtil.nowToSQLTimestamp())
                    .addParameter("id", user.getId())
                    .executeUpdate()
                    .getResult();
        } catch (Exception er) {
            er.printStackTrace();
        }
        return count == 1;
    }

    public boolean delete(User user) {
        String sql =
                "DELETE FROM USER WHERE id = :id";
        int count = 0;
        try (Connection conn = mSql.open()) {
            count = conn.createQuery(sql)
                    .addParameter("id", user.getId())
                    .executeUpdate()
                    .getResult();
        } catch (Exception er) {
            er.printStackTrace();
        }
        return count == 1;
    }

}
