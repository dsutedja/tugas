package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TestUserRepo {
    private UserRepository userRepository;

    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
    }


    @Before
    public void initialize() {
        userRepository = new UserRepository(dataSource);
        userRepository.deleteAll();
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
    }

    @Test
    public void testInsertUser() {
        User user = getFakeUser();

        long start = System.currentTimeMillis();
        userRepository.insert(user);
        System.out.println("Insert: elapsed: " + (System.currentTimeMillis() - start) + " ms");
        assert(user.getId() != -1);
    }

    @Test
    public void testFindUserByID() {
        User user = getFakeUser();
        userRepository.insert(user);

        User target = userRepository.findByUserID(user.getId());
        assert(target != null);
        assert(target.mLoadedFromDB == true);
        assert(target.getId() == user.getId());
    }

    @Test
    public void testFindUserByUsername() {
        User user = getFakeUser();
        userRepository.insert(user);

        User target = userRepository.findByUsername(user.getUsername());
        assert(target != null);
        assert(target.mLoadedFromDB == true);
        assert(target.getUsername().equals(user.getUsername()));
    }

    @Test
    public void testFindUserByNullUsername() {
        User user = getFakeUser();
        userRepository.insert(user);

        User target = userRepository.findByUsername(null);
        assert(target == null);
    }

    @Test
    public void testUpdateUser() {
        User user = getFakeUser();
        userRepository.insert(user);

        User after = userRepository.findByUserID(user.getId());
        after.setUsername("whatever");

        userRepository.update(after);
        User target = userRepository.findByUserID(after.getId());
        assert(target != null);
        assert(target.getUsername().equals("whatever"));
        assert(target.getUsername().equals(after.getUsername()));
    }

    @Test
    public void testDeleteUser() {
        User user = getFakeUser();
        userRepository.insert(user);

        boolean success = userRepository.delete(user);
        assert(success == true);

        User target = userRepository.findByUserID(user.getId());
        assert(target == null);
    }

    @Test
    public void testFindWrongUser() {
        User user = userRepository.findByUsername("foobar");
        assert(user == null);
    }

    @Test
    public void testFindUserWithWrongID() {
        User user = userRepository.findByUserID(-1);
        assert(user == null);
    }

    private User getFakeUser() {
        User user = new User();
        user.setUsername("dsfx");
        String salt = "smartphone";
        String password = "1HeadPhone";
        String encoded = PasswordUtil.md5(password, salt);
        user.setPassword(encoded);
        user.setSalt(salt);
        user.setLocked(false);
        user.setLoginAttempt(0);
        user.setCreationTime(System.currentTimeMillis());
        user.setLastMod(System.currentTimeMillis());
        return user;
    }

}
