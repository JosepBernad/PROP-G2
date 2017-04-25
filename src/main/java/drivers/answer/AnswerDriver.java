package drivers.answer;

import answer.Answer;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class AnswerDriver {

    private static final String LIST_ANSWERS = "1";
    private static final String GET_ANSWERS_BY_SURVEY_AND_USERNAME = "2";
    private static final String EXIT = "0";

    private static BufferedReader br;

    public static void main(String[] args) throws Exception {
        String option;
        do {
            System.out.println(LIST_ANSWERS + " - List answers");
            System.out.println(GET_ANSWERS_BY_SURVEY_AND_USERNAME + " - Get answers by survey and username");
            System.out.println(EXIT + " - Exit");
            br = new BufferedReader(new InputStreamReader(System.in));
            option = br.readLine();
            switch (option) {
                case LIST_ANSWERS:
                    listAnswers();
                    break;
                case GET_ANSWERS_BY_SURVEY_AND_USERNAME:
                    getAnswersBySurveyAndUsername();
                    break;
                default:
                    return;
            }
        } while (!option.equals(EXIT));
    }

    private static void getAnswersBySurveyAndUsername() throws Exception {
        String username = IOUtils.askForString("Enter username");
        Integer surveyId = IOUtils.askForInt("Enter survey id");
        List<Answer> answers = Answer.getAnswersByUsernameAndSurveyID(username, surveyId);
        for (Answer answer : answers) IOUtils.printContent(answer);
    }

    private static void listAnswers() {
        for (Answer answer : Answer.getAnswers()) IOUtils.printContent(answer);
    }
}