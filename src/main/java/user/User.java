package user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String USERS = "src/main/resources/users.json";

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public static Map<Integer, User> getUsers() {
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

    public void save() {
        Map<Integer, User> users = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    new File(USERS),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, User.class));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        users.put(this.id, this);

        try (FileWriter file = new FileWriter(USERS)) {
            file.write(new ObjectMapper().writeValueAsString(users));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getUserById(Integer id) {
        Map<Integer, User> users = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            users = mapper.readValue(
                    new File(USERS),
                    mapper.getTypeFactory().constructMapType(Map.class, Integer.class, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users.get(id);
    }
}
