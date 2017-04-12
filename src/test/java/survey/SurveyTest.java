package survey;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import question.NumericQuestion;
import user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SurveyTest {

    public static final Path SURVEY_BACKUP = Paths.get((Survey.SURVEY) + ".bak");
    public static final Path SURVEY_PATH = Paths.get(Survey.SURVEY);

    @Before
    public void backupUsersFile() throws IOException {
        Files.copy(SURVEY_PATH, SURVEY_BACKUP, REPLACE_EXISTING);
    }

    @After
    public void restoreUsersFile() throws IOException {
        Files.copy(SURVEY_BACKUP, SURVEY_PATH, REPLACE_EXISTING);
        Files.delete(SURVEY_BACKUP);
    }

    @Test
    public void test_givenAnExistingSurvey_whenAddQuestion_withValidQuestion_thenSurveyHasThisQuestion() {
        // Arrange
        Survey survey = new Survey();
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setStatement("Question1");

        // Act
        survey.addQuestion(numericQuestion);

        // Assert
        assertTrue(survey.getQuestions().contains(numericQuestion));
    }

    @Test
    public void test_givenNoSurveys_whenGetSurveys_thenReturnEmptyMap() {
        //Act
        Map<Integer,Survey> surveys = Survey.getSurveys();

        //Assert
        assertTrue(surveys.isEmpty());
    }

    @Test
    public void test_givenNoSurveys_whenSaveSurvey_withValidSurvey_thenPersistsSurvey() {
        //Arrange
        Survey survey = new Survey();
        survey.setId(1);
        survey.setName("Survey1");
        survey.setDescription("Description1");

        //Act
        survey.save();

        //Assert
        Map<Integer, Survey> surveys = Survey.getSurveys();
        assertEquals(survey,surveys.get(1));
    }
}
