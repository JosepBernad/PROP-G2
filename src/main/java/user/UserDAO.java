package user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {


    public static final String USERS = "src/main/resources/users.json";

    public Map<Integer, User> getUsers() {
        Map<Integer, User> users = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    new File(USERS),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void createUser(User user) {
        Map<Integer, User> users = getUsers();
        users.put(user.getId(), user);

        try (FileWriter file = new FileWriter(USERS)) {
            file.write(new ObjectMapper().writeValueAsString(users));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(Integer id) {
        return getUsers().get(id);
    }
}
