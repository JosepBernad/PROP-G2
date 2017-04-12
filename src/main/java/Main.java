import survey.Survey;

public class Main {

    public static void main(String[] args) {

        System.out.println("Welcome to ENQUESTATOR 3000"); // Display the string.
        Survey survey = new Survey();
        survey.setId(Integer.valueOf(args[0]));
        survey.setName(args[1]);
        survey.save();
    }
}
