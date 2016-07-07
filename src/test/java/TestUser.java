import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.ds.todo.models.User;
import com.ds.todo.models.UserRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

/**
 * Created by dsutedja on 7/7/16.
 */
public class TestUser {
    private static DataSource dataSource;
    private UserRepository userRepository;

    static {
        HikariConfig config = new HikariConfig("hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    @Before
    public void initialize() {
        userRepository = new UserRepository(dataSource);
    }

    @Test
    public void testSomething() {
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
        userRepository.insert(user);
    }
}
