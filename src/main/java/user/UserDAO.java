package user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserDAO {


    public static final String USERS = "src/main/resources/users.json";

    public Set<User> getUsers() {
        Set<User> users = new HashSet<User>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    new File(USERS),
                    mapper.getTypeFactory().constructCollectionType(Set.class, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

}
