package analysis;

import answer.Answer;
import answer.Answer.AnswerCollection;
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
import static org.junit.Assert.assertTrue;

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

    //    @Ignore
    @Test
    public void test() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/kMeansAnswers.json"), ANSWER_PATH, REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/kMeansSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);
        Files.copy(Paths.get("src/test/resources/kMeansUsers.json"), USER_PATH, REPLACE_EXISTING);

        Integer surveyId = 1;
        Integer nCoordinates = 4;
        AnswerCollection answerCollection = new AnswerCollection();
        List<Answer> user1 = Answer.getAnswersByUsernameAndSurveyID("User1", 1);
        List<Answer> user2 = Answer.getAnswersByUsernameAndSurveyID("User2", 1);
        List<Answer> user3 = Answer.getAnswersByUsernameAndSurveyID("User3", 1);
        List<Answer> user4 = Answer.getAnswersByUsernameAndSurveyID("User4", 1);

        addAnswers(answerCollection, user1);
        addAnswers(answerCollection, user2);
        addAnswers(answerCollection, user3);
        addAnswers(answerCollection, user4);

        KMeans kMeans = new KMeans(surveyId, answerCollection, nCoordinates);

        // Act
        List<Cluster> calc = kMeans.calc(2);

        // Assert
        assertTrue(calc.size() == 2);
    }

    private void addAnswers(AnswerCollection answerCollection, List<Answer> answers) {
        for (Answer answer : answers)
            answerCollection.addAnswer(answer);
    }
}
