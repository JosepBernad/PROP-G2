package drivers.survey;

import answer.*;
import exceptions.NotInRangeException;
import exceptions.RepeatedOptionWeightException;
import question.*;
import survey.Survey;
import user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SurveyDriver {

    private static final String LIST_SURVEYS = "1";
    private static final String CREATE_SURVEY = "2";
    private static final String DELETE_SURVEY = "3";
    private static final String ANSWER_SURVEY = "4";
    private static final String EXIT = "0";
    private static final String FREE_QUESTION = "1";
    private static final String MULTIVALUED_UNSORTED_QUALITATIVE_QUESTION = "2";
    private static final String NUMERIC_QUESTION = "3";
    private static final String SORTED_QUALITATIVE_QUESTION = "4";
    private static final String UNSORTED_QUALITATIVE_QUESTION = "5";
    private static final Boolean UNSORTED = Boolean.FALSE;
    private static final Boolean SORTED = Boolean.TRUE;
    private static BufferedReader br;
    private static User user;

    public static void main(String[] args) throws Exception {
        String option;
        do {
            System.out.println(LIST_SURVEYS + " - List surveys");
            System.out.println(CREATE_SURVEY + " - Create survey");
            System.out.println(DELETE_SURVEY + " - Delete survey");
            System.out.println(ANSWER_SURVEY + " - Answer survey");
            System.out.println(EXIT + " - Exit");
            br = new BufferedReader(new InputStreamReader(System.in));
            option = br.readLine();
            switch (option) {
                case LIST_SURVEYS:
                    listSurveys();
                    break;
                case CREATE_SURVEY:
                    createSurvey();
                    break;
                case DELETE_SURVEY:
                    deleteSurvey();
                    break;
                case ANSWER_SURVEY:
                    answerSurvey();
                    break;
                default:
                    return;
            }
        } while (!option.equals(EXIT));
    }

    private static void createSurvey() throws Exception {
        System.out.println();
        System.out.println("New Survey:");
        Survey survey = new Survey();
        System.out.print("Enter survey's title: ");
        survey.setTitle(br.readLine());
        System.out.print("Enter survey's description: ");
        survey.setDescription(br.readLine());
        System.out.println("Add questions: ");
        showQuestionTypes();
        String option = br.readLine();
        while (!EXIT.equals(option)) {
            switch (option) {
                case FREE_QUESTION:
                    FreeQuestion q1 = new FreeQuestion();
                    setQuestionStatement(q1);
                    System.out.print("Enter answer's maximum size: ");
                    q1.setMaxSize(Integer.valueOf(br.readLine()));
                    survey.addQuestion(q1);
                    break;

                case MULTIVALUED_UNSORTED_QUALITATIVE_QUESTION:
                    MultivaluedUnsortedQualitativeQuestion q2 = new MultivaluedUnsortedQualitativeQuestion();
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
            option = br.readLine();
        }
        survey.save();
    }

    private static void setQuestionStatement(Question question) throws IOException {
        System.out.println("Enter question's statement: ");
        question.setStatement(br.readLine());
    }

    private static void addOptions(QualitativeQuestion question, Boolean sorted) throws IOException, RepeatedOptionWeightException {
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
        String c = br.readLine();
        if ("Y".equals(c) || "y".equals(c)) addOptions(question, sorted);
    }

    private static void showQuestionTypes() {
        System.out.println();
        System.out.println("Choose one of the following types of questions:");
        System.out.println(FREE_QUESTION + " - Free Question");
        System.out.println(MULTIVALUED_UNSORTED_QUALITATIVE_QUESTION + " - Multivalued Unsorted Qualitative Question");
        System.out.println(NUMERIC_QUESTION + " - Numeric Question");
        System.out.println(SORTED_QUALITATIVE_QUESTION + " - Sorted Qualitative Question");
        System.out.println(UNSORTED_QUALITATIVE_QUESTION + " - Unsorted Qualitative Question");
        System.out.println(EXIT + " - Exit");
    }

    private static void deleteSurvey() throws IOException {
        Map<Integer, Survey> s = Survey.getSurveys();
        if (s.isEmpty()) System.out.println("Any existing survey");
        else {
            System.out.println("There are the following surveys:");
            for (Survey survey : s.values()) System.out.println(survey);
            System.out.println("Which survey do you want to delete?");
            int id = Integer.valueOf(br.readLine());
            if (s.containsKey(id)) {
                Survey.delete(id);
                System.out.println("Survey deleted successfully!");
            } else System.out.println("Not a valid number for a survey");
        }
    }

    private static void listSurveys() {
        Map<Integer, Survey> surveys = Survey.getSurveys();
        if (surveys.isEmpty()) System.out.println("Any existing survey");
        else {
            for (Survey survey : surveys.values()) System.out.println(survey);
        }
    }

    private static void answerSurvey() throws IOException {
        Map<Integer, Survey> s = Survey.getSurveys();
        if (s.isEmpty()) System.out.println("Any existing survey");
        else {
            System.out.print("Enter username: ");
            user = User.getUserByUsername(br.readLine());
            System.out.println("There are the following surveys:");
            for (Survey survey : s.values()) System.out.println(survey);
            System.out.print("Enter survey id: ");
            String selectedSurvey = br.readLine();
            Survey survey = Survey.getSurveyById(Integer.valueOf(selectedSurvey));
            if (survey != null) {
                HashSet<Answer> answers = new HashSet<>();
                for (Question question : survey.getQuestions()) {
                    System.out.println(question.getStatement());
                    if (question instanceof NumericQuestion) {
                        NumericAnswer numericAnswer = getNumericAnswer((NumericQuestion) question);
                        answers.add(numericAnswer);
                    } else if (question instanceof FreeQuestion) {
                        FreeAnswer freeAnswer = getFreeAnswer((FreeQuestion) question);
                        answers.add(freeAnswer);
                    } else if (question instanceof SortedQualitativeQuestion) {
                        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = getUnivaluedQualitativeAnswer((SortedQualitativeQuestion) question);
                        answers.add(univaluedQualitativeAnswer);
                    } else if (question instanceof UnsortedQualitativeQuestion) {
                        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = getUnivaluatedQualitativeAnswer((UnsortedQualitativeQuestion) question);
                        answers.add(univaluedQualitativeAnswer);
                    } else if (question instanceof MultivaluedUnsortedQualitativeQuestion) {
                        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = getMultivaluedQualitativeAnswer((MultivaluedUnsortedQualitativeQuestion) question);
                        answers.add(multivaluedQualitativeAnswer);
                    }
                }
                Answer.saveAnswersInFile(answers,Answer.ANSWERS);
            }
        }
    }

    private static MultivaluedQualitativeAnswer getMultivaluedQualitativeAnswer(MultivaluedUnsortedQualitativeQuestion question) throws IOException {
        System.out.println("Select max. " + question.getnMaxAnswers() + " of the following options:");
        List<Option> options = new ArrayList<>();
        options.addAll(question.getOptions());
        int i = 0;
        for (Option option : options) System.out.println(++i + ". " + option.getValue());
        Set<Option> opt = new HashSet<>();
        i = 0;
        do {
            Integer s = Integer.valueOf(br.readLine());
            opt.add(options.get(s-1));
            ++i;
        } while (i < question.getnMaxAnswers());
        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer.setOptions(opt);
        multivaluedQualitativeAnswer.setQuestionId(question.getId());
        multivaluedQualitativeAnswer.setUsername(user.getUsername());
        return multivaluedQualitativeAnswer;
    }

    private static UnivaluedQualitativeAnswer getUnivaluatedQualitativeAnswer(UnsortedQualitativeQuestion question) throws IOException {
        System.out.println("Select one of the following options");
        int i = 0;
        List<Option> optionsValuesOrderedByWeight = new ArrayList<>();
        optionsValuesOrderedByWeight.addAll(question.getOptions());
        for (Option option : optionsValuesOrderedByWeight) System.out.println(++i + ". " + option.getValue());
        Integer s = Integer.valueOf(br.readLine());
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(optionsValuesOrderedByWeight.get(s - 1));
        univaluedQualitativeAnswer.setQuestionId(question.getId());
        univaluedQualitativeAnswer.setUsername(user.getUsername());
        return univaluedQualitativeAnswer;
    }

    private static UnivaluedQualitativeAnswer getUnivaluedQualitativeAnswer(SortedQualitativeQuestion question) throws IOException {
        System.out.println("Select one of the following options");
        int i = 0;
        for (String value : question.optionsValuesOrderedByWeight()) System.out.println(++i + ". " + value);
        Integer s = Integer.valueOf(br.readLine());
        Option opt = new Option(question.optionsValuesOrderedByWeight().get(s - 1), s - 1);
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(opt);
        univaluedQualitativeAnswer.setQuestionId(question.getId());
        univaluedQualitativeAnswer.setUsername(user.getUsername());
        return univaluedQualitativeAnswer;
    }

    private static FreeAnswer getFreeAnswer(FreeQuestion question) throws IOException {
        System.out.println("Enter your answer, max characters: " + question.getMaxSize());
        FreeAnswer freeAnswer = new FreeAnswer();
        freeAnswer.setValue(br.readLine());
        freeAnswer.setQuestionId(question.getId());
        freeAnswer.setUsername(user.getUsername());
        return freeAnswer;
    }

    private static NumericAnswer getNumericAnswer(NumericQuestion question) throws IOException {
        System.out.println("Enter a number between " + question.getMin().toString() + " and " + question.getMax().toString() + ":");
        NumericAnswer numericAnswer = null;
        try {
            numericAnswer = question.makeAnAnswer(Integer.valueOf(br.readLine()));
        } catch (NotInRangeException e) {
            e.printStackTrace();
        }
        numericAnswer.setQuestionId(question.getId());
        numericAnswer.setMin(question.getMin());
        numericAnswer.setMax(question.getMax());
        numericAnswer.setUsername(user.getUsername());
        return numericAnswer;
    }
}
