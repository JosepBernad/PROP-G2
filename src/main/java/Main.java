import question.*;
import survey.Survey;
import user.DuplicatedUsernameException;
import user.EmptyRequiredAttributeException;
import user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

public class Main {

    private static final String WELCOME_MESSAGE = "Welcome to ENQUESTATOR 3000";
    public static final int CREATE_NEW_USER = 1;
    private static final int CREATE_NEW_SURVEY = 1;
    private static final int LIST_EXISTING_SURVEYS = 2;
    private static final int EXIT = 0;
    private static final int FREE_QUESTION = 1;
    private static final int MULTIEVALUATED_UNSORTED_QUALITATIVE_QUESTION = 2;
    private static final int NUMERIC_QUESTION = 3;
    private static final int SORTED_QUALITATIVE_QUESTION = 4;
    private static final int UNSORTED_QUALITATIVE_QUESTION = 5;
    private static final Boolean UNSORTED = Boolean.FALSE;
    private static final Boolean SORTED = Boolean.TRUE;
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

        showMainMenu();

        Integer option = Integer.valueOf(br.readLine());
        while (option != EXIT) {
            switch (option) {
                case CREATE_NEW_SURVEY:
                    createSurvey();
                    break;
                case LIST_EXISTING_SURVEYS:
                    Map<Integer, Survey> surveys = Survey.getSurveys();
                    for (Survey survey : surveys.values()) System.out.println(survey.getTitle());
                    break;
                default:
                    System.out.println("Invalid option");
            }
            showMainMenu();
            option = Integer.valueOf(br.readLine());
        }
    }

    private static void createSurvey() throws IOException {
        System.out.println();
        System.out.println("New Survey:");
        Survey survey = new Survey();
        System.out.print("Enter survey's title: ");
        survey.setTitle(br.readLine());
        System.out.print("Enter survey's description: ");
        survey.setDescription(br.readLine());
        System.out.println("Add questions: ");
        showQuestionTypes();
        Integer option = Integer.valueOf(br.readLine());
        while (option != EXIT) {
            switch (option) {
                case FREE_QUESTION:
                    FreeQuestion q1 = new FreeQuestion();
                    setQuestionStatement(q1);
                    System.out.print("Enter answer's maximum size: ");
                    q1.setMaxSize(Integer.valueOf(br.readLine()));
                    survey.addQuestion(q1);
                    break;
                case MULTIEVALUATED_UNSORTED_QUALITATIVE_QUESTION:
                    MultievaluatedUnsortedQualitativeQuestion q2 = new MultievaluatedUnsortedQualitativeQuestion();
                    setQuestionStatement(q2);
                    addOptions(q2, UNSORTED);
                    System.out.print("Enter maximum number of selectable options: ");
                    q2.setnMaxAnswers(Integer.valueOf(br.readLine()));
                    survey.addQuestion(q2);
                    break;
                case NUMERIC_QUESTION:
                    NumericQuestion q3 = new NumericQuestion();
                    setQuestionStatement(q3);
                    System.out.print("Enter answer's minimum value: ");
                    q3.setMin(Integer.valueOf(br.readLine()));
                    System.out.print("Enter answer's maximum value: ");
                    q3.setMax(Integer.valueOf(br.readLine()));
                    survey.addQuestion(q3);
                    break;
                case SORTED_QUALITATIVE_QUESTION:
                    SortedQualitativeQuestion q4 = new SortedQualitativeQuestion();
                    setQuestionStatement(q4);
                    addOptions(q4, SORTED);
                    survey.addQuestion(q4);
                    break;
                case UNSORTED_QUALITATIVE_QUESTION:
                    UnsortedQualitativeQuestion q5 = new UnsortedQualitativeQuestion();
                    setQuestionStatement(q5);
                    addOptions(q5, UNSORTED);
                    survey.addQuestion(q5);
                    break;
                default:
                    System.out.println("Invalid option");
            }
            showQuestionTypes();
            option = Integer.valueOf(br.readLine());
        }
        survey.setId(new Random().nextInt(100));
        survey.save();
    }

    private static void setQuestionStatement(Question question) throws IOException {
        System.out.println("Enter question's statement: ");
        question.setStatement(br.readLine());
    }

    private static void showMainMenu() {
        System.out.println();
        System.out.println("Choose one of the following options:");
        System.out.println("1 - Create new survey");
        System.out.println("2 - List existing surveys");
        System.out.println("0 - Exit");
    }

    private static void showQuestionTypes() {
        System.out.println();
        System.out.println("Choose one of the following types of questions:");
        System.out.println("1 - Free Question");
        System.out.println("2 - Multievaluated Unsorted Question");
        System.out.println("3 - Numeric Question");
        System.out.println("4 - Sorted Qualitative Question");
        System.out.println("5 - Unsorted Qualitative Question");
        System.out.println("0 - Exit");
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

    private static void addOptions(QualitativeQuestion question, Boolean sorted) throws IOException {
        System.out.print("Enter option's value: ");
        String value = br.readLine();
        Option option;
        if (sorted) {
            System.out.print("Enter option's weight: ");
            option = new Option(value, Integer.valueOf(br.readLine()));
        } else {
            option = new Option(value);
        }
        question.addOption(option);

        System.out.println("¿Would you like to add another option? (Y/N)");
        if ("Y".equals(br.readLine())) addOptions(question, sorted);
    }
}
