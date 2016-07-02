package com.ds.todo.models;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by esutedja on 7/1/16.
 */
public class TestTaskRepo {
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    private static DataSource dataSource;
    private User fakeUser;

    static {
        HikariConfig config = new HikariConfig("hikari_test.properties");
        dataSource = new HikariDataSource(config);
    }


    @Before
    public void initialize() {
        userRepository = new UserRepository(dataSource);
        userRepository.deleteAll();
        fakeUser = getFakeUser();
        userRepository.insert(fakeUser);

        taskRepository = new TaskRepository(dataSource);
        taskRepository.deleteAll();
    }

    @After
    public void destroy() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void testInsert() {
        Task fake = getFakeTask();
        Task target = taskRepository.insert(fake);
        assert(target != null);
        assert(target.getId() != -1);
        fake.setId(target.getId());
        assert(target.equals(fake));
    }

    @Test
    public void testDelete() {
        Task fake = getFakeTask();
        Task target = taskRepository.insert(fake);
        assert(target != null);

        boolean success = taskRepository.delete(target);
        assert(success);
    }

    @Test
    public void testUpdate() {
        Task fake = getFakeTask();
        taskRepository.insert(fake);

        Task after = taskRepository.findByTaskID(fake.getId());
        assert(after != null);
        after.setTitle("Title changed!!");
        after.setState(Task.State.PENDING);
        Task target = taskRepository.update(after);
        assert(target != null);
        assert(target.getId() == after.getId());
        assert(target.getTitle().equals("Title changed!!"));
        assert(target.getState() == Task.State.PENDING);
    }

    @Test
    public void testFindById() {
        Task fake = getFakeTask();
        taskRepository.insert(fake);

        Task target = taskRepository.findByTaskID(fake.getId());
        assert(target != null);
        assert(target.equals(fake));
    }

    @Test
    public void testFindByUserID() {
        Task fake = getFakeTask();
        fake.setTitle("Task #1");

        Task fake2 = getFakeTask();
        fake2.setTitle("Task #2");

        Task fake3 = getFakeTask();
        fake3.setTitle("Task #3");

        taskRepository.insert(fake);
        taskRepository.insert(fake2);
        taskRepository.insert(fake3);

        List<Task> tasks = taskRepository.findByUserId(fakeUser.getId());
        assert(tasks != null && tasks.size() == 3);
        assert(getTask(tasks, fake.getId()).equals(fake));
        assert(getTask(tasks, fake2.getId()).equals(fake2));
        assert(getTask(tasks, fake3.getId()).equals(fake3));
    }

    private Task getTask(List<Task> tasks, int id) {
        Task retVal = null;
        for (Task task : tasks) {
            if (task.getId() == id) {
                retVal = task;
            }
        }
        return retVal;
    }

    @Test
    public void testFindByUserAndState() {
        Task fake = getFakeTask();
        fake.setTitle("Task #1");
        fake.setState(Task.State.COMPLETED);

        Task fake2 = getFakeTask();
        fake2.setTitle("Task #2");
        fake2.setState(Task.State.COMPLETED);

        Task fake3 = getFakeTask();
        fake3.setTitle("Task #3");
        fake3.setState(Task.State.PENDING);

        taskRepository.insert(fake);
        taskRepository.insert(fake2);
        taskRepository.insert(fake3);

        List<Task> tasks = taskRepository.findByUserIdAndState(fakeUser.getId(), Task.State.COMPLETED);
        assert(tasks != null && tasks.size() == 2);
        assert(getTask(tasks, fake.getId()).equals(fake));
        assert(getTask(tasks, fake2.getId()).equals(fake2));

        List<Task> tasks2 = taskRepository.findByUserIdAndState(fakeUser.getId(), Task.State.PENDING);
        assert(tasks2 != null && tasks2.size() == 1);
        assert(getTask(tasks2, fake3.getId()).equals(fake3));
    }

    private Task getFakeTask() {
        Task task = new Task();
        task.setState(Task.State.COMPLETED);
        task.setDescription("Test Task Description");
        task.setId(-1);
        task.setUserId(fakeUser.getId());
        task.setLastModified(DatesUtil.nowToSQLTimestamp());
        task.setTitle("Test Task Title");
        return task;
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
