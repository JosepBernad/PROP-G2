package user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserServiceTest {

    public static final Path USERS_BACKUP = Paths.get((UserDAO.USERS) + ".bak");
    public static final Path USERS_PATH = Paths.get(UserDAO.USERS);
    private UserService userService = new UserService();


    @Before
    public void backupUsersFile() throws IOException {
        Files.copy(USERS_PATH, USERS_BACKUP, REPLACE_EXISTING);
    }

    @After
    public void restoreUsersFile() throws IOException {
        Files.copy(USERS_BACKUP, USERS_PATH, REPLACE_EXISTING);
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
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        Set<User> users = userService.getUsers();

        // Assert
        Set<User> expectedUsers = new HashSet<>();
        addUser(expectedUsers, 1, "Pepito");
        addUser(expectedUsers, 3, "Borja");
        addUser(expectedUsers, 2, "Pepita");
        assertEquals(expectedUsers, users);
    }

    private void addUser(Set<User> expectedUsers, int id, String name) {
        User user = new User();
        user.setName(name);
        user.setId(id);
        expectedUsers.add(user);
    }

}
