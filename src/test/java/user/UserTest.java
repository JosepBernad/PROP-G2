package user;

import org.junit.After;
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

    private static final Path USERS_PATH = Paths.get(User.USERS);

    @After
    public void restoreUsersFile() throws IOException {
        Files.deleteIfExists(USERS_PATH);
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

    @Test(expected = DuplicatedUsernameException.class)
    public void test_givenExistingUser_whenSaveUser_withSameUsername_thenThrowsDuplicatedUsernameException() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);
        User user = new User();
        user.setUsername("pepi");
        user.setName("Pepi Diaz");

        // Act
        user.save();
    }

    @Test(expected = EmptyRequiredAttributeException.class)
    public void test_givenNewUser_whenSaveUser_withEmptyUserName_thenThrowsEmptyRequiredAttributeException() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("");

        // Act
        user.save();
    }

    @Test(expected = EmptyRequiredAttributeException.class)
    public void test_givenNewUser_whenSaveUser_withEmptyName_thenThrowsEmptyRequiredAttributeException() throws Exception {
        // Arrange
        User user = new User();
        user.setName("");

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
