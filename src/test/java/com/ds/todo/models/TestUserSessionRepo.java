package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Properties;

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

    @Before
    public void initialize() {
        userRepository = new UserRepository(dataSource);
        userRepository.deleteAll();

        theUser = getFakeUser();
        userRepository.insert(theUser);

        sessionRepository = new UserSessionRepository(dataSource);
        sessionRepository.deleteAll();
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    public void testInsert() {
        UserSession session = getFakeSession();

        sessionRepository.insert(session);
        assert(session.getId() != -1);
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

    private User getFakeUser() {
        User user = new User();
        user.setUsername("dsfx");
        String salt = "smartphone";
        String password = "1HeadPhone";
        String encoded = PasswordUtil.md5(password, salt);
        user.setPassword(encoded);
        user.setSalt(salt);
        String now = DatesUtil.nowToSQLTimestamp();
        user.setCreationDate(now);
        user.setLastMod(now);
        return user;
    }

    private UserSession getFakeSession() {
        UserSession session = new UserSession();
        session.setUserID(theUser.getId());
        session.setSessionId("098125khjasf");
        session.setLastLogin(DatesUtil.nowToSQLTimestamp());
        session.setTimeOut(1000);
        return session;
    }
}
