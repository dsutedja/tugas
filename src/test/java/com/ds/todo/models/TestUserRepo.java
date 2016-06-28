package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.ds.todo.models.User;
import com.ds.todo.models.UserRepository;
import javafx.util.converter.DateStringConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TestUserRepo {
    private UserRepository userRepository;

    @Before
    public void initialize() {
        userRepository = new UserRepository("jdbc:mysql://64.90.60.189:3306/ds_todos_testenv");
        userRepository.deleteAll();
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
    }

    @Test
    public void testInsertUser() {
        User user = getFakeUser();

        userRepository.insert(user);
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
        String now = DatesUtil.nowToSQLTimestamp();
        user.setCreationDate(now);
        user.setLastMod(now);
        return user;
    }

}
