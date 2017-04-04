package user;

import java.util.Map;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public Map<Integer, User> getUsers() {
        return userDAO.getUsers();
    }

    public void createUser(User user) {
        userDAO.createUser(user);
    }

    public User getUserById(Integer id) {
        return userDAO.getUserById(id);
    }
}
