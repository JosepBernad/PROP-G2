package question;

import answer.FreeAnswer;
import exceptions.InvalidSizeException;
import exceptions.NotInRangeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FreeQuestionTest {

    @Test(expected = InvalidSizeException.class)
    public void test_givenFreeQuestion_whenMakeAnAnswer_withMoreSizeThanAllowed_thenThrowsInvalidSizeException() throws Exception {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(5);

        // Act
        freeQuestion.makeAnAnswer("miquel");
    }

    @Test
    public void test_givenFreeQuestion_whenMakeAnAnswer_withEmptyString_thenReturnsAnswerWithNullValue() throws Exception {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(5);

        // Act
        FreeAnswer freeAnswer = freeQuestion.makeAnAnswer("");

        // Arrange
        assertNull(freeAnswer.getValue());
    }

    @Test
    public void test_givenFreeQuestion_whenMakeAnAnswer_withValidString_thenReturnsFreeAnswerWithString() throws Exception {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();
        freeQuestion.setMaxSize(8);

        // Act
        FreeAnswer freeAnswer = freeQuestion.makeAnAnswer("value");

        // Assert
        assertEquals("value", freeAnswer.getValue());
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenFreeQuestion_whenSetMaxSize_withZeroValue_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();

        // Act
        freeQuestion.setMaxSize(0);
    }

    @Test(expected = NotInRangeException.class)
    public void test_givenFreeQuestion_whenSetMaxSize_withNegativeValue_thenThrowsNotInRangeException() throws NotInRangeException {
        // Arrange
        FreeQuestion freeQuestion = new FreeQuestion();

        // Act
        freeQuestion.setMaxSize(-1);
    }
}
