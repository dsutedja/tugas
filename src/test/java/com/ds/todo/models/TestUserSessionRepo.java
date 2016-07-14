package com.ds.todo.models;

import com.ds.todo.utils.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

import static org.junit.Assert.assertTrue;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TestUserSessionRepo {
    private UserRepository userRepository;
    private UserSessionRepository sessionRepository;
    private User theUser;
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
    }

    private void clearDB() {
        // let's clear the test DB clean now
        Sql2o engine = new Sql2o(dataSource);
        try (Connection conn = engine.open()) {
            String sql = "delete from USER";
            conn.createQuery(sql).executeUpdate();
            sql = "delete from TASK";
            conn.createQuery(sql).executeUpdate();
            sql = "delete from USER_SESSION";
            conn.createQuery(sql).executeUpdate();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    @Before
    public void initialize() {
        clearDB();
        userRepository = new UserRepository(dataSource);

        theUser = getFakeUser();
        userRepository.insert(theUser);

        sessionRepository = new UserSessionRepository(dataSource);
    }

    @After
    public void destroy() {
        clearDB();
    }

    @Test
    public void testInsert() {
        UserSession session = getFakeSession();

        sessionRepository.insert(session);
        assert(session.getId() != -1);
    }

    @Test
    public void testInsertDuplicate() {
        UserSession session = getFakeSession();
        sessionRepository.insert(session);

        UserSession target = sessionRepository.insert(session);
        assert(target == null);
    }

    @Test
    public void testDelete() {
        UserSession session = getFakeSession();
        sessionRepository.insert(session);
        sessionRepository.delete(session);
        UserSession target = sessionRepository.findBySessionID(session.getSessionId());
        assert(target == null);
    }

    @Test
    public void testUpdate() {
        UserSession session = getFakeSession();
        sessionRepository.insert(session);

        UserSession after = sessionRepository.findByUserID(theUser.getId());
        assert(after != null);
        after.setSessionId("u19857198257198275891275");

        sessionRepository.update(after);

        UserSession target = sessionRepository.findByUserID(theUser.getId());
        assert(target != null);
        assert(target.getSessionId().equals("u19857198257198275891275"));
    }

    @Test
    public void testFindByUsername() {
        UserSession session = getFakeSession();
        sessionRepository.insert(session);

        UserSession target = sessionRepository.findByUserID(theUser.getId());
        assert(target != null);
        assert(target.getUserID() == theUser.getId());
        assert(target.getSessionId().equals(session.getSessionId()));
        assert(target.mLoadedFromDB == true);
    }

    @Test
    public void testFindBySessionID() {
        UserSession session = getFakeSession();
        sessionRepository.insert(session);

        UserSession target = sessionRepository.findBySessionID(session.getSessionId());
        assert(target != null);
        assert(target.getUserID() == theUser.getId());
        assert(target.getSessionId().equals(session.getSessionId()));
        assert(target.mLoadedFromDB == true);
    }

    @Test
    public void testFindInvalidSessionID() {
        UserSession target = sessionRepository.findBySessionID("jhaskjhaskjghaksjhg");
        assert(target == null);
    }

    @Test
    public void testFindInvalidUserID() {
        UserSession target = sessionRepository.findByUserID(-1);
        assert(target == null);
    }

    @Test
    public void testFindByNullSessionID() {
        boolean gotException = false;

        try {
            sessionRepository.findBySessionID(null);
        } catch (Exception er) {
            gotException = true;
        }

        assertTrue(gotException);
    }

    @Test
    public void testUpdateWithNull() {
        boolean gotException = false;
        try {
            sessionRepository.update(null);
        } catch (Exception er) {
            gotException = true;
        }
        assertTrue(gotException);
    }

    @Test
    public void testInsertWithNull() {
        boolean gotException = false;
        try {
            sessionRepository.insert(null);
        } catch (Exception er) {
            gotException = true;
        }
        assertTrue(gotException);
    }

    @Test
    public void testDeleteWithNull() {
        boolean gotException = false;
        try {
            sessionRepository.delete(null);
        } catch (Exception er) {
            gotException = true;
        }
        assertTrue(gotException);
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

    private UserSession getFakeSession() {
        UserSession session = new UserSession();
        session.setUserID(theUser.getId());
        session.setSessionId("098125khjasf");
        session.setCreationTime(System.currentTimeMillis());
        session.setTimeOut(1000);
        return session;
    }
}
