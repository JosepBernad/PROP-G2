package question;

import answer.FreeAnswer;
import exceptions.InvalidSizeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FreeQuestionTest {

    @Test(expected = InvalidSizeException.class)
    public void test_givenFreeQuestion_whenMakeAnAnswer_withMoreSizeThanAllowed_thenThrowsInvalidSizeException() throws InvalidSizeException {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(5);

        // Act
        freeQuestion.makeAnAnswer("miquel");
    }

    @Test
    public void test_givenFreeQuestion_whenMakeAnAnswer_withEmptyString_thenReturnsAnswerWithNullValue() throws InvalidSizeException {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(5);

        // Act
        FreeAnswer freeAnswer = freeQuestion.makeAnAnswer("");

        // Arrange
        assertNull(freeAnswer.getValue());
    }

    @Test
    public void test_givenFreeQuestion_whenMakeAnAnswer_withValidString_thenReturnsFreeAnswerWithString() throws InvalidSizeException {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(8);

        // Act
        FreeAnswer freeAnswer = freeQuestion.makeAnAnswer("value");

        // Assert
        assertEquals("value", freeAnswer.getValue());
    }
}
