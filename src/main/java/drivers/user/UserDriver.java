package drivers.user;

import user.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserDriver {

    private static final String SHOW_ALL_USERS = "1";
    private static final String GET_USER_BY_USERNAME = "2";
    private static final String CREATE_USER = "3";
    private static final String EXIT = "0";
    private static BufferedReader br;

    public static void main(String[] args) throws Exception {
        String option;
        do {
            System.out.println(SHOW_ALL_USERS + " - Show all users");
            System.out.println(GET_USER_BY_USERNAME + " - Get user by username");
            System.out.println(CREATE_USER + " - Create user");
            System.out.println(EXIT + " - Exit");
            br = new BufferedReader(new InputStreamReader(System.in));
            option = br.readLine();
            switch (option) {
                case SHOW_ALL_USERS:
                    showAllUsers();
                    break;
                case GET_USER_BY_USERNAME:
                    getUserByUsername();
                    break;
                case CREATE_USER:
                    createUser();
                    break;
                default:
                    return;
            }
        } while (!option.equals(EXIT));
    }

    private static void getUserByUsername() throws Exception {
        System.out.print("Enter username: ");
        User user = User.getUserByUsername(br.readLine());
        if (user == null) System.out.println("User not found");
        else System.out.println(user);
    }

    private static void createUser() throws Exception {
        System.out.print("Enter username: ");
        String username = br.readLine();
        System.out.print("Enter name: ");
        String name = br.readLine();
        User user = new User(username, name);
        user.save();
    }

    private static void showAllUsers() {
        for (User user : User.getUsers().values()) {
            System.out.println(user);
        }
    }

}
