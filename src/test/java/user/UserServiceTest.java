package user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {

    public static final Path USERS_BACKUP = Paths.get("src/main/resources/users.txt" + ".bak");
    private UserService userService = new UserService();


    @Before
    public void backupUsersFile() throws IOException {
        Files.copy(UserDAO.USERS, USERS_BACKUP, REPLACE_EXISTING);
    }

    @After
    public void restoreUsersFile() throws IOException {
        Files.copy(USERS_BACKUP, UserDAO.USERS, REPLACE_EXISTING);
        Files.delete(USERS_BACKUP);
    }

    @Test
    public void test_givenNonUsers_whenGetUsers_thenReturnsEmptySet() {
        Set<User> users = userService.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void test_givenUsers_whenGetUsers_thenReturnsSetWithUsers() throws IOException {
        // Arrange
        List<String> lines = Arrays.asList("Pepito", "Josep");
        Files.write(UserDAO.USERS, lines, Charset.forName("UTF-8"));

        // Act
        Set<User> users = userService.getUsers();

        // Assert
        assertTrue(users.size() == 2);
    }

}
