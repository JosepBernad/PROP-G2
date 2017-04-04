package user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
    public void test_givenNoUsers_whenGetUsers_thenReturnsEmptyMap() {
        Map<Integer, User> users = userService.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void test_givenUsers_whenGetUsers_thenReturnsSetWithUsers() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        Map<Integer, User> users = userService.getUsers();

        // Assert
        Map<Integer, User> expectedUsers = new HashMap<>();
        addUserToMap(expectedUsers, 1, "Pepito");
        addUserToMap(expectedUsers, 3, "Borja");
        addUserToMap(expectedUsers, 2, "Pepita");
        assertEquals(expectedUsers, users);
    }

    @Test
    public void test_givenExistingUser_whenGetUser_withValidId_thenReturnsUser() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        User user = userService.getUserById(1);

        // Assert
        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setName("Pepito");
        assertEquals(expectedUser, user);
    }

    @Test
    public void test_givenNoUsers_whenCreateUser_withValidUser_thenPersistsUser() {
        // Arrange
        User user = new User();
        user.setId(28);
        user.setName("Miquel");

        //Act
        userService.createUser(user);

        //Assert
        assertEquals(user, userService.getUserById(28));
    }

    private void addUserToMap(Map<Integer, User> expectedUsers, int id, String name) {
        User user = new User();
        user.setName(name);
        user.setId(id);
        expectedUsers.put(id, user);
    }

}
