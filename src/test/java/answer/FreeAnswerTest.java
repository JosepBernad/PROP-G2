package answer;

import org.junit.Test;
import question.FreeQuestion;
import survey.Survey;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FreeAnswerTest {

    @Test
    public void test_whenParseSentence_withSentence_thenReturnsListWithLowercaseWords() {
        String sentence = "Casa importante Sergio";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"casa", "importante", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_whenParseSentence_withSentenceWithDots_thenReturnsListWithoutDots() {
        String sentence = "Casa importante Sergio.";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"casa", "importante", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_whenParseSentence_withSentenceWithCommas_thenReturnsListWithoutCommas() {
        String sentence = "Casa, importante Sergio,";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"casa", "importante", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_whenParseSentence_withSentenceWithFunctionalWords_thenReturnsListWithoutFunctionalWords() {
        String sentence = "I am a math student";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"math", "student"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_givenFreeAnswer_whenCalculateDistance_withAnotherFreeAnswer_thenReturnsDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        FreeQuestion question = new FreeQuestion();
        question.setMaxSize(255);
        survey.addQuestion(question);
        survey.save();

        FreeAnswer freeAnswer1 = new FreeAnswer();
        freeAnswer1.setSurveyId(1);
        freeAnswer1.setQuestionId(0);
        freeAnswer1.setValue("pepa");

        FreeAnswer freeAnswer2 = new FreeAnswer();
        freeAnswer2.setSurveyId(1);
        freeAnswer2.setQuestionId(0);
        freeAnswer2.setValue("pepe");

        // Act
        Double distance = freeAnswer1.calculateDistance(freeAnswer2);

        // Assert
        assertEquals((Double) 0.25D, distance);
    }

    @Test
    public void test_givenFreeAnswer_whenCalculateDistance_withAnotherFreeAnswer_thenReturnsMinimumDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        FreeQuestion question = new FreeQuestion();
        question.setMaxSize(255);
        survey.addQuestion(question);
        survey.save();

        FreeAnswer freeAnswer1 = new FreeAnswer();
        freeAnswer1.setSurveyId(1);
        freeAnswer1.setQuestionId(0);
        freeAnswer1.setValue("casa");

        FreeAnswer freeAnswer2 = new FreeAnswer();
        freeAnswer2.setSurveyId(1);
        freeAnswer2.setQuestionId(0);
        freeAnswer2.setValue("casa");

        // Act
        Double distance = freeAnswer1.calculateDistance(freeAnswer2);

        // Assert
        assertEquals((Double) 0D, distance);
    }

    @Test
    public void test_givenFreeAnswer_whenCalculateDistance_withAnotherFreeAnswer_thenReturnsMaximumDistance() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("title");
        FreeQuestion question = new FreeQuestion();
        question.setMaxSize(255);
        survey.addQuestion(question);
        survey.save();

        FreeAnswer freeAnswer1 = new FreeAnswer();
        freeAnswer1.setSurveyId(1);
        freeAnswer1.setQuestionId(0);
        freeAnswer1.setValue("tete");

        FreeAnswer freeAnswer2 = new FreeAnswer();
        freeAnswer2.setSurveyId(1);
        freeAnswer2.setQuestionId(0);
        freeAnswer2.setValue("papa");

        // Act
        Double distance = freeAnswer1.calculateDistance(freeAnswer2);

        // Assert
        assertEquals((Double) 1D, distance);
    }

}
