package user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String USERS = "users.json";

    private String username;
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Map<String, User> getUsers() {
        Map<String, User> users = new HashMap<>();

        File file = new File(USERS);
        if (!file.exists()) return users;

        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    file, mapper.getTypeFactory().constructMapType(Map.class, String.class, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void save() throws NotValidUsernameException {
        Map<String, User> users = getUsers();

        if (users.containsKey(this.username)) throw new NotValidUsernameException();

        users.put(this.username, this);

        File file = new File(USERS);
        try {
            if (!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new ObjectMapper().writeValueAsString(users));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getUserByUsername(String username) {
        return getUsers().get(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
