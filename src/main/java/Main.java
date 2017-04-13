import user.DuplicatedUsernameException;
import user.EmptyRequiredAttributeException;
import user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static final String WELCOME_MESSAGE = "Welcome to ENQUESTATOR 3000";
    private static BufferedReader br;

    public static void main(String[] args) throws IOException {

        System.out.println(WELCOME_MESSAGE);

        br = new BufferedReader(new InputStreamReader(System.in));
        User user = new User();
        System.out.print("Enter the username for the new user: ");
        user.setUsername(br.readLine());
        System.out.print("Enter the name: ");
        user.setName(br.readLine());

        saveUser(user);

        showMenu();

        Integer option = Integer.valueOf(br.readLine());
        while (option != 3) {
            switch (option) {
                case 1:
                    System.out.println(user);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invalid option");
            }
            showMenu();
            option = Integer.valueOf(br.readLine());
        }

//        NumericQuestion numericQuestion = new NumericQuestion();
//        numericQuestion.setMax(10);
//        numericQuestion.setMin(0);
//        numericQuestion.setStatement("Nota:");
//
//        Survey survey = new Survey();
//        survey.setId(Integer.valueOf(args[0]));
//        survey.setName(args[1]);
//
//        survey.addQuestion(numericQuestion);
//        survey.save();
//
//        User user = new User();
//        user.setUsername(args[2]);
//        user.setName(args[3]);
//        user.save();
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("Choose one of the following options:");
        System.out.println("1 - Create new survey");
        System.out.println("2 - List existing surveys");
        System.out.println("3 - Exit");
    }

    private static void saveUser(User user) throws IOException {
        try {
            user.save();
        } catch (DuplicatedUsernameException e) {
            System.out.println("Username is already taken");
            System.out.print("Enter a new one: ");
            user.setUsername(br.readLine());
            saveUser(user);
        } catch (EmptyRequiredAttributeException e) {
            e.printStackTrace();
        }
    }
}
