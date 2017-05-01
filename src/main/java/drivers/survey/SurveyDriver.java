package drivers.survey;

import answer.*;
import exceptions.NotInRangeException;
import exceptions.RepeatedOptionWeightException;
import question.*;
import survey.Survey;
import user.User;
import utils.IOUtils;

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
            IOUtils.printLine(LIST_SURVEYS + " - List surveys");
            IOUtils.printLine(CREATE_SURVEY + " - Create survey");
            IOUtils.printLine(DELETE_SURVEY + " - Delete survey");
            IOUtils.printLine(ANSWER_SURVEY + " - Answer survey");
            IOUtils.printLine(EXIT + " - Exit");
            br = new BufferedReader(new InputStreamReader(System.in));
            option = IOUtils.askForString("Enter your option");
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
        IOUtils.drawLine();
        IOUtils.printLine("New Survey:");
        Survey survey = new Survey();
        survey.setTitle(IOUtils.askForString("Enter survey's title"));
        survey.setDescription(IOUtils.askForString("Enter survey's description"));
        IOUtils.printLine("Add questions: ");
        showQuestionTypes();
        String option = IOUtils.askForString("Enter your option");
        while (!EXIT.equals(option)) {
            switch (option) {
                case FREE_QUESTION:
                    FreeQuestion q1 = new FreeQuestion();
                    setQuestionStatement(q1);
                    q1.setMaxSize(IOUtils.askForInt("Enter answer's maximum size"));
                    survey.addQuestion(q1);
                    break;

                case MULTIVALUED_UNSORTED_QUALITATIVE_QUESTION:
                    MultivaluedUnsortedQualitativeQuestion q2 = new MultivaluedUnsortedQualitativeQuestion();
                    setQuestionStatement(q2);
                    addOptions(q2, UNSORTED);
                    q2.setnMaxAnswers(IOUtils.askForInt("Enter maximum number of selectable options"));
                    survey.addQuestion(q2);
                    break;

                case NUMERIC_QUESTION:
                    NumericQuestion q3 = new NumericQuestion();
                    setQuestionStatement(q3);
                    q3.setMin(IOUtils.askForInt("Enter answer's minimum value"));
                    q3.setMax(IOUtils.askForInt("Enter answer's maximum value"));
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
                    IOUtils.printLine("Invalid option");
            }
            showQuestionTypes();
            option = IOUtils.askForString("Enter your option");
        }
        survey.save();
    }

    private static void setQuestionStatement(Question question) throws IOException {
        question.setStatement(IOUtils.askForString("Enter question's statement"));
    }

    private static void addOptions(QualitativeQuestion question, Boolean sorted) throws IOException, RepeatedOptionWeightException {
        String value = IOUtils.askForString("Enter option's value: ");
        Option option;
        if (sorted) {
            option = new Option(value, IOUtils.askForInt("Enter option's weight: "));
        } else {
            option = new Option(value);
        }
        question.addOption(option);

        String c = IOUtils.askForString("¿Would you like to add another option? (Y/N)");
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
        if (s.isEmpty()) IOUtils.printLine("Any existing survey");
        else {
            IOUtils.printLine("There are the following surveys:");
            for (Survey survey : s.values()) IOUtils.printContent(survey);
            int id = IOUtils.askForInt("Which survey do you want to delete?");
            if (s.containsKey(id)) {
                Survey.delete(id);
                IOUtils.printLine("Survey deleted successfully!");
            } else IOUtils.printLine("Not a valid number for a survey");
        }
    }

    private static void listSurveys() {
        Map<Integer, Survey> surveys = Survey.getSurveys();
        if (surveys.isEmpty()) IOUtils.printContent("Any existing survey");
        else {
            for (Survey survey : surveys.values()) IOUtils.printContent(survey);
        }
    }

    private static void answerSurvey() throws IOException {
        Map<Integer, Survey> s = Survey.getSurveys();
        if (s.isEmpty()) System.out.println("Any existing survey");
        else {
            user = User.getUserByUsername(IOUtils.askForString("Enter username"));
            IOUtils.printLine("There are the following surveys");
            for (Survey survey : s.values()) IOUtils.printContent(survey);
            Integer integer = IOUtils.askForInt("Enter survey id");
            Survey survey = Survey.getSurveyById(integer);
            if (survey != null) {
                HashSet<Answer> answers = new HashSet<>();
                for (Question question : survey.getQuestions()) {
                    IOUtils.printLine(question.getStatement());
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
                Answer.saveAnswersInFile(answers);
            }
        }
    }

    private static MultivaluedQualitativeAnswer getMultivaluedQualitativeAnswer(MultivaluedUnsortedQualitativeQuestion question) throws IOException {
        IOUtils.printLine("Select max. " + question.getnMaxAnswers() + " of the following options");
        List<Option> options = new ArrayList<>();
        options.addAll(question.getOptions());
        int i = 0;
        for (Option option : options) IOUtils.printLine(++i + ". " + option.getValue());
        Set<Option> opt = new HashSet<>();
        i = 0;
        boolean exit = false;
        do {
            Integer s = IOUtils.askForInt("Enter id or 0 to finish");
            if (s == 0) exit = true;
            else {
                opt.add(options.get(s - 1));
                ++i;
            }
        } while (!exit && i < question.getnMaxAnswers());
        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer.setOptions(opt);
        multivaluedQualitativeAnswer.setQuestionId(question.getId());
        multivaluedQualitativeAnswer.setUsername(user.getUsername());
        return multivaluedQualitativeAnswer;
    }

    private static UnivaluedQualitativeAnswer getUnivaluatedQualitativeAnswer(UnsortedQualitativeQuestion question) throws IOException {
        IOUtils.printLine("Select one of the following options");
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
        IOUtils.printLine("Select one of the following options");
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
        FreeAnswer freeAnswer = new FreeAnswer();
        freeAnswer.setValue(IOUtils.askForString("Enter your answer, max characters " + question.getMaxSize()));
        freeAnswer.setQuestionId(question.getId());
        freeAnswer.setUsername(user.getUsername());
        return freeAnswer;
    }

    private static NumericAnswer getNumericAnswer(NumericQuestion question) throws IOException {
        Integer value = IOUtils.askForIntInRange("Enter a number between " + question.getMin().toString() + " and " + question.getMax().toString(), question.getMin(), question.getMax());
        NumericAnswer numericAnswer = null;
        try {
            numericAnswer = question.makeAnAnswer(value);
            numericAnswer.setQuestionId(question.getId());
            numericAnswer.setSurveyId(1);
            numericAnswer.setUsername(user.getUsername());
        } catch (NotInRangeException e) {
            e.printStackTrace();
        }
        return numericAnswer;
    }
}
