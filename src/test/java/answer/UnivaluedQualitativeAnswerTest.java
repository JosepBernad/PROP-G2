package answer;

import org.junit.After;
import org.junit.Test;
import question.Option;
import question.SortedQualitativeQuestion;
import question.UnsortedQualitativeQuestion;
import survey.Survey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class UnivaluedQualitativeAnswerTest {

    private static final Path SURVEY_PATH = Paths.get(Survey.SURVEYS);

    @After
    public void deleteSurveysFile() throws IOException {
        Files.deleteIfExists(SURVEY_PATH);
    }

    @Test
    public void test_givenUnivaluedQualitativeAnswerFromSortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsMaxDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        question.addOption(new Option("mucho", 0));
        question.addOption(new Option("normal", 1));
        question.addOption(new Option("poco", 2));
        survey.addQuestion(question);
        survey.save();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer1 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer1.setSurveyId(1);
        univaluedQualitativeAnswer1.setQuestionId(0);
        univaluedQualitativeAnswer1.setOption(new Option("mucho", 0));

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer2 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer2.setSurveyId(1);
        univaluedQualitativeAnswer2.setQuestionId(0);
        univaluedQualitativeAnswer2.setOption(new Option("poco", 2));

        // Act
        Double distance = univaluedQualitativeAnswer1.calculateDistance(univaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 1D, distance);
    }

    @Test
    public void test_givenUnivaluedQualitativeAnswerFromSortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        question.addOption(new Option("mucho", 0));
        question.addOption(new Option("normal", 1));
        question.addOption(new Option("poco", 2));
        survey.addQuestion(question);
        survey.save();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer1 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer1.setSurveyId(1);
        univaluedQualitativeAnswer1.setQuestionId(0);
        univaluedQualitativeAnswer1.setOption(new Option("mucho", 0));

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer2 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer2.setSurveyId(1);
        univaluedQualitativeAnswer2.setQuestionId(0);
        univaluedQualitativeAnswer2.setOption(new Option("normal", 1));

        // Act
        Double distance = univaluedQualitativeAnswer1.calculateDistance(univaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 0.5D, distance);
    }

    @Test
    public void test_givenUnivaluedQualitativeAnswerFromUnsortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        UnsortedQualitativeQuestion question = new UnsortedQualitativeQuestion();
        question.addOption(new Option("verde"));
        question.addOption(new Option("azul"));
        question.addOption(new Option("rojo"));
        survey.addQuestion(question);
        survey.save();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer1 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer1.setSurveyId(1);
        univaluedQualitativeAnswer1.setQuestionId(0);
        univaluedQualitativeAnswer1.setOption(new Option("verde"));

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer2 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer2.setSurveyId(1);
        univaluedQualitativeAnswer2.setQuestionId(0);
        univaluedQualitativeAnswer2.setOption(new Option("verde"));

        // Act
        Double distance = univaluedQualitativeAnswer1.calculateDistance(univaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 0D, distance);
    }

    @Test
    public void test_givenUnivaluedQualitativeAnswerFromUnsortedQuestion_whenCalculateDistance_withAnotherUnivaluedQualitativeAnswer_thenReturnsMaxDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        UnsortedQualitativeQuestion question = new UnsortedQualitativeQuestion();
        question.addOption(new Option("verde"));
        question.addOption(new Option("azul"));
        question.addOption(new Option("rojo"));
        survey.addQuestion(question);
        survey.save();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer1 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer1.setSurveyId(1);
        univaluedQualitativeAnswer1.setQuestionId(0);
        univaluedQualitativeAnswer1.setOption(new Option("verde"));

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer2 = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer2.setSurveyId(1);
        univaluedQualitativeAnswer2.setQuestionId(0);
        univaluedQualitativeAnswer2.setOption(new Option("azul"));

        // Act
        Double distance = univaluedQualitativeAnswer1.calculateDistance(univaluedQualitativeAnswer2);

        // Assert
        assertEquals((Double) 1D, distance);
    }
}
