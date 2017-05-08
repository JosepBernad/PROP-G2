package answer;

import org.junit.After;
import org.junit.Test;
import question.Option;
import question.SortedQualitativeQuestion;
import survey.Survey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MultivaluedQualitativeAnswerTest {

    private static final Path SURVEY_PATH = Paths.get(Survey.SURVEYS);

    @After
    public void deleteSurveysFile() throws IOException {
        Files.deleteIfExists(SURVEY_PATH);
    }

    @Test
    public void test_givenMultivaluedQualitativeAnswerFromSortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsMaxDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        Option verde = new Option("verde");
        question.addOption(verde);
        Option azul = new Option("azul");
        question.addOption(azul);
        Option rojo = new Option("rojo");
        question.addOption(rojo);
        survey.addQuestion(question);
        survey.save();

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer1 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer1.setSurveyId(1);
        multivaluedQualitativeAnswer1.setQuestionId(0);
        Set<Option> set1 = new HashSet<>();
        set1.add(verde);
        multivaluedQualitativeAnswer1.setOptions(set1);

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer2 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer2.setSurveyId(1);
        multivaluedQualitativeAnswer2.setQuestionId(0);
        Set<Option> set2 = new HashSet<>();
        set2.add(verde);
        set2.add(azul);
        multivaluedQualitativeAnswer2.setOptions(set2);

        // Act
        Double distance = multivaluedQualitativeAnswer1.calculateDistance(multivaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 0.5D, distance);
    }

    @Test
    public void test_givenMultivaluedQualitativeAnswerFromSortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        Option verde = new Option("verde");
        question.addOption(verde);
        Option azul = new Option("azul");
        question.addOption(azul);
        Option rojo = new Option("rojo");
        question.addOption(rojo);
        survey.addQuestion(question);
        survey.save();

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer1 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer1.setSurveyId(1);
        multivaluedQualitativeAnswer1.setQuestionId(0);
        Set<Option> set1 = new HashSet<>();
        set1.add(verde);
        multivaluedQualitativeAnswer1.setOptions(set1);

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer2 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer2.setSurveyId(1);
        multivaluedQualitativeAnswer2.setQuestionId(0);
        Set<Option> set2 = new HashSet<>();
        set2.add(rojo);
        set2.add(azul);
        multivaluedQualitativeAnswer2.setOptions(set2);

        // Act
        Double distance = multivaluedQualitativeAnswer1.calculateDistance(multivaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 1D, distance);
    }

    @Test
    public void test_givenMultivaluedQualitativeAnswerFromSortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsMinimumDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        Option verde = new Option("verde");
        question.addOption(verde);
        Option azul = new Option("azul");
        question.addOption(azul);
        Option rojo = new Option("rojo");
        question.addOption(rojo);
        survey.addQuestion(question);
        survey.save();

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer1 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer1.setSurveyId(1);
        multivaluedQualitativeAnswer1.setQuestionId(0);
        Set<Option> set1 = new HashSet<>();
        set1.add(rojo);
        set1.add(verde);
        set1.add(azul);
        multivaluedQualitativeAnswer1.setOptions(set1);

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer2 = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer2.setSurveyId(1);
        multivaluedQualitativeAnswer2.setQuestionId(0);
        Set<Option> set2 = new HashSet<>();
        set2.add(rojo);
        set2.add(verde);
        set2.add(azul);
        multivaluedQualitativeAnswer2.setOptions(set2);

        // Act
        Double distance = multivaluedQualitativeAnswer1.calculateDistance(multivaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 0D, distance);
    }

}
