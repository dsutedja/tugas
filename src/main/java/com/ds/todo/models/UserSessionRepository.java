package com.ds.todo.models;

import com.ds.todo.utils.IDGenerator;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dsutedja on 6/21/16.
 */
public class UserSessionRepository {
    private static final int RETRY_THRESHOLD = 10;
    private static final Logger LOGGER = Logger.getLogger(UserSessionRepository.class.getName());

    private Sql2o mSql;

    public UserSessionRepository(DataSource dataSource) {
        mSql = new Sql2o(dataSource);
    }

    public UserSession findBySessionID(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("Invalid session ID: cannot be null or empty");
        }

        String sql = "SELECT * from USER_SESSION WHERE session_id = :session_id";
        List<UserSession> result = null;
        try (Connection conn = mSql.open()) {
            result = conn.createQuery(sql)
                    .addParameter("session_id", sessionId)
                    .addColumnMapping("user_id", "userId")
                    .addColumnMapping("session_id", "sessionId")
                    .addColumnMapping("created", "creationTime")
                    .executeAndFetch(UserSession.class);
        } catch (Exception er) {
            er.printStackTrace();
        }

        UserSession retVal = null;
        if (result != null) {
            if (result.size() > 1) {
                // TODO: use logger
                System.out.println("ERR: more than 1 session with same session ID");
                // TODO: maybe delete the offending sessions??
            } else if (!result.isEmpty()) {
                retVal = result.get(0);
                retVal.mLoadedFromDB = true;
            }
        }
        return retVal;
    }

    public UserSession findByUserID(int userID) {
        String sql = "SELECT * FROM USER_SESSION WHERE user_id = :user_id";

        List<UserSession> result = null;
        try (Connection conn = mSql.open()) {
            result = conn.createQuery(sql)
                    .addParameter("user_id", userID)
                    .addColumnMapping("user_id", "userId")
                    .addColumnMapping("session_id", "sessionId")
                    .addColumnMapping("created", "creationTime")
                    .executeAndFetch(UserSession.class);
        } catch (Exception er) {
            er.printStackTrace();
        }

        UserSession retVal = null;
        if (result != null) {
            if (result.size() > 1) {
                // TODO: use logger
                System.out.println("ERR: more than 1 session with same session ID");
                // TODO: maybe delete the offending sessions??
            } else if (!result.isEmpty()) {
                retVal = result.get(0);
                retVal.mLoadedFromDB = true;
            }
        }
        return retVal;
    }


    public boolean update(UserSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Cannot update null session");
        }

        String sql =
                "UPDATE USER_SESSION SET "
                        + "session_id = :session_id, "
                        + "created = :created, "
                        + "timeout = :timeout "
                        + "WHERE id = :id";
        int retVal = -1;
        try (Connection conn = mSql.open()){
            retVal = conn.createQuery(sql)
                    .addParameter("session_id", session.getSessionId())
                    .addParameter("created", session.getCreationTime())
                    .addParameter("timeout", session.getTimeOut())
                    .addParameter("id", session.getId())
                    .executeUpdate()
                    .getResult();

        } catch (Exception er) {
            er.printStackTrace();
        }
        return retVal > 0;
    }

    public UserSession insert(UserSession session) {
        if (session == null || session.mLoadedFromDB) {
            throw new IllegalArgumentException("Cannot insert null or DB loaded session");
        }

        String sql =
                "INSERT INTO USER_SESSION (id, user_id, session_id, created, timeout)"
                + " values (:id, :user_id, :session_id, :created, :timeout)";

        int count = 0;
        try (Connection conn = mSql.open()) {
            int retry = 0;
            while (count == 0 && retry <= RETRY_THRESHOLD) {
                retry++;
                int randomID = IDGenerator.nextID();
                count = conn.createQuery(sql)
                        .addParameter("id", randomID)
                        .addParameter("user_id", session.getUserID())
                        .addParameter("session_id", session.getSessionId())
                        .addParameter("created", session.getCreationTime())
                        .addParameter("timeout", session.getTimeOut())
                        .executeUpdate()
                        .getResult();
                session.setId(randomID);
            }
        } catch (Exception er) {
            System.out.println("WARNING: failed to insert, message: " + er.getMessage());
        }

        return count > 0 ? session : null;
    }

    public boolean delete(UserSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Cannot delete null session");
        }

        String sql =
                "DELETE FROM USER_SESSION WHERE ID = :id and SESSION_ID = :session_id";
        int retVal = -1;
        try (Connection conn = mSql.open()) {
            retVal = conn.createQuery(sql)
                    .addParameter("id", session.getId())
                    .addParameter("session_id", session.getSessionId())
                    .executeUpdate()
                    .getResult();
        } catch (Exception er) {
            er.printStackTrace();
        }
        return retVal > 0;
    }

}
