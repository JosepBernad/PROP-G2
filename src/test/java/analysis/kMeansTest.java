package analysis;

import answer.Answer;
import org.junit.After;
import org.junit.Test;
import survey.Survey;
import user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;

public class kMeansTest {

    private static final Path ANSWER_PATH = Paths.get(Answer.ANSWERS);
    private static final Path SURVEY_PATH = Paths.get(Survey.SURVEYS);
    private static final Path USER_PATH = Paths.get(User.USERS);


    @After
    public void deleteFiles() throws IOException {
        Files.deleteIfExists(ANSWER_PATH);
        Files.deleteIfExists(SURVEY_PATH);
        Files.deleteIfExists(USER_PATH);
    }

    @Test
    public void test_kmeans_with2clusters() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/kMeansAnswers.json"), ANSWER_PATH, REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/kMeansSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/kMeansUsers.json"), USER_PATH, REPLACE_EXISTING);

        Integer surveyId = 1;
        Integer nCoordinates = 4;

        KMeans kMeans = new KMeans(surveyId, nCoordinates);

        // Act
        List<Cluster> calc = kMeans.calc(2);

        // Assert
        assertEquals(2, calc.size());
    }

}
