package survey;


import Exceptions.EmptyRequiredAttributeException;
import org.junit.After;
import org.junit.Test;
import question.NumericQuestion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SurveyTest {

    private static final Path SURVEY_PATH = Paths.get(Survey.SURVEYS);

    @After
    public void deleteSurveysFile() throws IOException {
        Files.deleteIfExists(SURVEY_PATH);
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
    public void test_givenSurveys_whenGetSurveys_thenReturnsMapWithSurveys() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);

        // Act
        Map<Integer, Survey> surveys = Survey.getSurveys();

        // Assert
        Map<Integer, Survey> expectedSurveys = new HashMap<>();
        addSurveyToMap(expectedSurveys, 1, "Survey1");
        addSurveyToMap(expectedSurveys, 2, "Survey2");
        addSurveyToMap(expectedSurveys, 3, "Survey3");
        assertEquals(expectedSurveys, surveys);
    }

    @Test
    public void test_givenExistingSurveys_whenGetSurveyById_thenReturnThisSurvey() throws IOException {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);

        // Act
        Survey survey = Survey.getSurveyById(2);

        // Assert
        Survey expectedSurvey = new Survey();
        expectedSurvey.setId(2);
        expectedSurvey.setTitle("Survey2");
        assertEquals(expectedSurvey, survey);
    }

    @Test
    public void test_givenExistingSurveys_whenSaveSurvey_withNewSurvey_thenPersistsItWithNextID() throws Exception {
        // Arrange
        Files.copy(Paths.get("src/test/resources/SampleSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);
        Survey survey = new Survey();
        survey.setTitle("Survey");
        survey.setDescription("Description");

        // Act
        survey.save();

        // Assert
        assertEquals(survey, Survey.getSurveyById(4));
    }

    @Test
    public void test_givenNoSurveys_whenSaveSurvey_withValidSurvey_thenPersistsSurvey() throws EmptyRequiredAttributeException {
        //Arrange
        Survey survey = new Survey();
        survey.setId(5);
        survey.setTitle("Survey");
        survey.setDescription("Description");

        //Act
        survey.save();

        //Assert
        assertEquals(survey, Survey.getSurveyById(5));
    }

    @Test
    public void test_givenNoSurveys_whenSaveSurvey_withValidSurvey_thenPersistsSurveyWithID1() throws EmptyRequiredAttributeException {
        //Arrange
        Survey survey = new Survey();
        survey.setTitle("Survey1");
        survey.setDescription("Description1");

        //Act
        survey.save();

        //Assert
        assertEquals(survey, Survey.getSurveyById(1));
    }

    @Test(expected = EmptyRequiredAttributeException.class)
    public void test_givenNewSurvey_whenSaveSurvey_withEmptyTitle_thenThrowsEmptyRequiredAttributeException() throws Exception {
        // Arrange
        Survey survey = new Survey();
        survey.setTitle("");

        // Act
        survey.save();
    }

    @Test
    public void test_givenExistingSurveys_whenDeletesSurvey_withValidSurveyId_thenDeletesSurvey() throws IOException {
        //Arrange
        Files.copy(Paths.get("src/test/resources/SampleSurveys.json"), SURVEY_PATH, REPLACE_EXISTING);

        //Act
        Survey.delete(1);

        //Assert
        assertFalse(Survey.getSurveys().containsKey(1));
    }

    private void addSurveyToMap(Map<Integer, Survey> expectedSurveys, Integer id, String name) {
        Survey survey = new Survey();
        survey.setId(id);
        survey.setTitle(name);
        expectedSurveys.put(id, survey);
    }

}
