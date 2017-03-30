package user;

import java.util.Set;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public Set<User> getUsers() {
        return userDAO.getUsers();
    }
}
