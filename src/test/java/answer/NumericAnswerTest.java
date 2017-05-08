package answer;

import org.junit.After;
import org.junit.Test;
import question.NumericQuestion;
import survey.Survey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class NumericAnswerTest {

    private static final Path SURVEY_PATH = Paths.get(Survey.SURVEYS);

    @After
    public void deleteSurveysFile() throws IOException {
        Files.deleteIfExists(SURVEY_PATH);
    }

    @Test
    public void test_givenNumericAnswer_whenCalculateDistance_withAnotherNumericAnswer_thenReturnsDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        NumericQuestion question = new NumericQuestion();
        question.setMax(10);
        question.setMin(0);
        survey.addQuestion(question);
        survey.save();

        NumericAnswer numericAnswer1 = new NumericAnswer();
        numericAnswer1.setSurveyId(1);
        numericAnswer1.setQuestionId(0);
        numericAnswer1.setValue(2D);

        NumericAnswer numericAnswer2 = new NumericAnswer();
        numericAnswer2.setSurveyId(1);
        numericAnswer2.setQuestionId(0);
        numericAnswer2.setValue(6D);

        // Act
        Double distance = numericAnswer1.calculateDistance(numericAnswer2);

        // Assert
        assertEquals((Double) 0.4D, distance);
    }
}
