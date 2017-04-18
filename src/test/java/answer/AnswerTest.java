package answer;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
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

    private void addAnswerToSet(Set<Answer> expectedAnswers, Integer questionId, String username) {
        Answer answer = new NumericAnswer();
        answer.setQuestionId(questionId);
        answer.setUsername(username);
        expectedAnswers.add(answer);
    }

    @Test
    public void test_givenNewAnswer_whenSave_thenPersistsIt() {
        //Arrange
        Answer answer = new NumericAnswer();
        answer.setQuestionId(1);
        answer.setUsername("pepito");

        //Act
        answer.save();

        //Assert
        assertTrue(Answer.getAnswers().contains(answer));
    }

}
