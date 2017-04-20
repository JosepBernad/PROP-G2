package answer;

import org.junit.After;
import org.junit.Test;

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
import static org.junit.Assert.assertTrue;

public class AnswerTest {

    private static final Path ANSWERS_PATH = Paths.get(Answer.ANSWERS);

    @After
    public void restoreUsersFile() throws IOException {
        Files.deleteIfExists(ANSWERS_PATH);
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
    public void test_givenNewAnswer_whenSave_thenPersistsIt() {
        // Arrange
        Answer answer = new NumericAnswer();
        answer.setQuestionId(1);
        answer.setUsername("pepito");

        // Act
        answer.save();

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
        Answer answer1 = new NumericAnswer();
        answer1.setQuestionId(1);
        answer1.setUsername("");
        Answer answer2 = new NumericAnswer();
        answer2.setQuestionId(2);
        answer2.setUsername("");
        Answer answer3 = new NumericAnswer();
        answer3.setQuestionId(3);
        answer3.setUsername("");
        Answer answer4 = new NumericAnswer();
        answer4.setQuestionId(4);
        answer4.setUsername("");
        List<Answer> expectedList = Arrays.asList(answer1, answer2, answer3, answer4);

        assertEquals(expectedList, orderedById);

    }

}
