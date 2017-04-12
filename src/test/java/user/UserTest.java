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

public class UserTest {

    private static final Path USERS_BACKUP = Paths.get((User.USERS) + ".bak");
    private static final Path USERS_PATH = Paths.get(User.USERS);


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
        Map<String, User> users = User.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void test_givenUsers_whenGetUsers_thenReturnsMapWithUsers() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        Map<String, User> users = User.getUsers();

        // Assert
        Map<String, User> expectedUsers = new HashMap<>();
        addUserToMap(expectedUsers, "pepi", "Pepito");
        addUserToMap(expectedUsers, "borji", "Borja");
        addUserToMap(expectedUsers, "peti", "Petita");
        assertEquals(expectedUsers, users);
    }

    @Test
    public void test_givenExistingUser_whenGetUser_withValidId_thenReturnsUser() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        User user = User.getUserByUsername("pepi");

        // Assert
        User expectedUser = new User();
        expectedUser.setUsername("pepi");
        expectedUser.setName("Pepito");
        assertEquals(expectedUser, user);
    }

    @Test
    public void test_givenNoUsers_whenSaveUser_withValidUser_thenPersistsUser()  throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("miqui");
        user.setName("Miquel");

        //Act
        user.save();

        //Assert
        assertEquals(user, User.getUserByUsername("miqui"));
    }

    @Test(expected = NotValidUsernameException.class)
    public void test_givenExistingUser_whenSaveUser_withSameUsername_thenThrowsNotValidUsernameException() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);
        User user = new User();
        user.setUsername("pepi");

        // Act
        user.save();
    }

    private void addUserToMap(Map<String, User> expectedUsers, String username, String name) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        expectedUsers.put(username, user);
    }

}
