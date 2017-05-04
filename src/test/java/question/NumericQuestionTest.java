package question;

import answer.NumericAnswer;
import exceptions.NotInRangeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericQuestionTest {

    @Test(expected = NotInRangeException.class)
    public void test_givenNumericQuestion_whenMakeAnAnswer_withValueSmallerThanAllowed_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMin(2);
        numericQuestion.setMax(7);

        // Act
        numericQuestion.makeAnAnswer(-3D);
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenNumericQuestion_whenMakeAnAnswer_withValueGreaterThanAllowed_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMin(2);
        numericQuestion.setMax(7);

        // Act
        numericQuestion.makeAnAnswer(8D);
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenNumericQuestion_whenSetMin_withGreaterValueThanMax_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMax(2);

        // Act
        numericQuestion.setMin(3);
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenNumericQuestion_whenSetMax_withSmallerValueThanMin_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMin(2);

        // Act
        numericQuestion.setMax(1);
    }

    @Test
    public void test_givenNumericQuestion_whenMakeAnAnswer_withValidValue_thenReturnsAnswerWithQuestionId() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setId(1);
        numericQuestion.setMin(2);
        numericQuestion.setMax(4);

        // Act
        NumericAnswer numericAnswer = numericQuestion.makeAnAnswer(3D);

        // Assert
        assertEquals((Integer) 1, numericAnswer.getQuestionId());
    }
}
