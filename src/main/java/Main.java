import survey.Survey;
import user.NotValidUsernameException;
import user.User;

public class Main {

    public static void main(String[] args) throws NotValidUsernameException {

        System.out.println("Welcome to ENQUESTATOR 3000"); // Display the string.
        Survey survey = new Survey();
        survey.setId(Integer.valueOf(args[0]));
        survey.setName(args[1]);
        survey.save();

        User user = new User();
        user.setUsername(args[2]);
        user.setName(args[3]);
        user.save();
    }
}
