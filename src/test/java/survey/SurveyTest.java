package survey;


import org.junit.Test;
import question.NumericQuestion;

import static org.junit.Assert.assertTrue;

public class SurveyTest {

    @Test
    public void test_givenAnExistingSurvey_whenAddQuestion_withValidQuestion_thenSurveyHasThisQuestion()
    {
        // Arrange
        Survey survey = new Survey();
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setStatement("Question1");

        // Act
        survey.addQuestion(numericQuestion);

        // Assert
        assertTrue(survey.getQuestions().contains(numericQuestion));
    }


    //@Test
    //public void test_given
}
