package user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDAO {

    public static final Path USERS = Paths.get("src/main/resources/users.txt");

    public Set<User> getUsers() {

        Set<User> users = new HashSet<>();
        Path file = USERS;
        List<String> strings = new ArrayList<>();
        try {
            strings = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String name : strings) {
            User user = new User();
            user.setName(name);
            users.add(user);
        }
        return users;
    }
}
