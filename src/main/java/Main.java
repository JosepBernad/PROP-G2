import question.NumericQuestion;
import survey.Survey;
import user.NotValidUsernameException;
import user.User;

public class Main {

    public static void main(String[] args) throws NotValidUsernameException {

        System.out.println("Welcome to ENQUESTATOR 3000"); // Display the string.

        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMax(10);
        numericQuestion.setMin(0);
        numericQuestion.setStatement("Nota:");

        Survey survey = new Survey();
        survey.setId(Integer.valueOf(args[0]));
        survey.setName(args[1]);

        survey.addQuestion(numericQuestion);
        survey.save();

        User user = new User();
        user.setUsername(args[2]);
        user.setName(args[3]);
        user.save();
    }
}
