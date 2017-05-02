package question;

import answer.UnivaluedQualitativeAnswer;
import exceptions.NotValidOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnsortedQualitativeQuestionTest {

    @Test
    public void test_givenUnsortedQualitativeQuestion_whenMakeAnAnswer_withValidOption_thenReturnUnivaluedQualitativeAnswer() throws Exception {
        // Arrange
        UnsortedQualitativeQuestion unsortedQualitativeQuestion = new UnsortedQualitativeQuestion();
        Option option = new Option("verd");
        unsortedQualitativeQuestion.addOption(option);

        // Act
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = unsortedQualitativeQuestion.makeAnAnswer(option);

        // Assert
        assertEquals("verd", univaluedQualitativeAnswer.getOption().getValue());
    }


    @Test(expected = NotValidOptionException.class)
    public void test_givenUnsortedQualitativeQuestion_whenMakeAnAnswer_withNotValidOption_thenThrowsNotValidOptionException() throws NotValidOptionException {
        // Arrange
        UnsortedQualitativeQuestion unsortedQualitativeQuestion = new UnsortedQualitativeQuestion();
        Option option = new Option("verd", 2);

        // Act
        unsortedQualitativeQuestion.makeAnAnswer(option);
    }
}
