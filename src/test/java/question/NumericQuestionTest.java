package question;

import answer.FreeAnswer;
import answer.NumericAnswer;
import exceptions.InvalidSizeException;
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
        numericQuestion.makeAnAnswer(-3);
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenNumericQuestion_whenMakeAnAnswer_withValueGreaterThanAllowed_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMin(2);
        numericQuestion.setMax(7);

        // Act
        numericQuestion.makeAnAnswer(8);
    }

    @Test
    public void test_givenNumericQuestion_whenMakeAnAnswer_withValidInteger_thenReturnsNumericAnswerWithThisInteger() throws NotInRangeException {
        // Arrange
        NumericQuestion numericQuestion = new NumericQuestion();
        numericQuestion.setMin(2);
        numericQuestion.setMax(7);

        // Act
        NumericAnswer numericAnswer = numericQuestion.makeAnAnswer(4);

        // Assert
        assertEquals((Integer) 4, numericAnswer.getValue());
    }
}
