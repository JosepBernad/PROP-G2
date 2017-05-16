package answer;

import exceptions.ResourceNotFoundException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import survey.Survey;
import user.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AnswerTest {

    private static final Path ANSWERS_PATH = Paths.get(Answer.ANSWERS);
    private static final Path USERS_PATH = Paths.get(User.USERS);
    private static final Path SURVEYS_PATH = Paths.get(Survey.SURVEYS);

    @After
    public void restoreFiles() throws IOException {
        Files.deleteIfExists(ANSWERS_PATH);
        Files.deleteIfExists(USERS_PATH);
        Files.deleteIfExists(SURVEYS_PATH);
    }

    @Test
    public void test_givenExistingAnswers_whenGetAnswers_thenReturnsAnswers() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleAnswers.json"), ANSWERS_PATH, REPLACE_EXISTING);

        // Act
        Set<Answer> answers = Answer.getAnswers();

        // Assert
        Set<Answer> expectedAnswers = new HashSet<>();
        addAnswerToSet(expectedAnswers, 1, "pepito");
        addAnswerToSet(expectedAnswers, 2, "pepita");
        addAnswerToSet(expectedAnswers, 3, "pepiti");
        assertEquals(expectedAnswers, answers);
    }

    private void addAnswerToSet(Set<Answer> answers, Integer questionId, String username) {
        Answer answer = new NumericAnswer();
        answer.setQuestionId(questionId);
        answer.setUsername(username);
        answers.add(answer);
    }

    @Test
    public void test_givenNewAnswer_whenSaveAnswersInFile_thenPersistsIt() {
        // Arrange
        Answer answer = new NumericAnswer();
        answer.setSurveyId(1);
        answer.setQuestionId(1);
        answer.setUsername("pepito");

        Set<Answer> answers = new HashSet<>();
        answers.add(answer);
        
        // Act
        Answer.saveAnswersInFile(answers);

        // Assert
        assertTrue(Answer.getAnswers().contains(answer));
    }

    @Test
    public void test_givenSetOfAnswers_whenGetOrderedById_thenReturnsListOfAnswersOrderedByQuestionId() {
        // Arrange
        Set<Answer> answers = new HashSet<>();
        addAnswerToSet(answers, 1, "");
        addAnswerToSet(answers, 3, "");
        addAnswerToSet(answers, 4, "");
        addAnswerToSet(answers, 2, "");

        // Act
        List<Answer> orderedById = Answer.getOrderedById(answers);

        // Assert
        Answer answer1 = createAnswerWithIdAndEmptyUsername(1);
        Answer answer2 = createAnswerWithIdAndEmptyUsername(2);
        Answer answer3 = createAnswerWithIdAndEmptyUsername(3);
        Answer answer4 = createAnswerWithIdAndEmptyUsername(4);
        List<Answer> expectedList = Arrays.asList(answer1, answer2, answer3, answer4);

        assertEquals(expectedList, orderedById);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_whenGetAnswersBySurveyAndUsername_withNonExistingSurvey_thenThrowsResourceNotFoundException() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleUsers.json"), USERS_PATH, REPLACE_EXISTING);

        // Act
        Answer.getAnswersByUsernameAndSurveyID("pepi", 1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_whenGetAnswersBySurveyAndUsername_withNonExistingUser_thenThrowsResourceNotFoundException() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleSurveys.json"), SURVEYS_PATH, REPLACE_EXISTING);

        // Act
        Answer.getAnswersByUsernameAndSurveyID("pepi", 1);
    }

    @Test
    public void test_givenNonAnswers_whenImportAnswers_withValidJsonFile_thenPersistsAnswers() throws IOException {
        // Arrange
        String jsonPath = "src/test/resources/SampleAnswers2.json";

        NumericAnswer n1 = new NumericAnswer();
        n1.setSurveyId(1);
        n1.setUsername("pepito");
        n1.setQuestionId(0);
        n1.setValue(100.0);

        NumericAnswer n2 = new NumericAnswer();
        n2.setSurveyId(1);
        n2.setUsername("pepita");
        n2.setQuestionId(0);
        n2.setValue(200.0);

        // Act
        Answer.importAnswers(jsonPath);

        // Assert
        assertTrue(Answer.getAnswers().contains(n1));
        assertTrue(Answer.getAnswers().contains(n2));
    }

    @Test(expected = FileNotFoundException.class)
    public void test_givenExistingAnswers_whenImportAnswers_withInvalidPath_thenThrowsFileNotFoundException() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleAnswers2.json"), ANSWERS_PATH, REPLACE_EXISTING);
        String answerPath = "src/test/resources/InvalidPath.json";

        // Act
        Answer.importAnswers(answerPath);
    }

    private Answer createAnswerWithIdAndEmptyUsername(Integer questionId) {
        Answer answer = new NumericAnswer();
        answer.setQuestionId(questionId);
        answer.setUsername("");
        return answer;
    }

}