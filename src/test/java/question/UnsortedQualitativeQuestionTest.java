package question;

import answer.UnivaluedQualitativeAnswer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnsortedQualitativeQuestionTest {

    @Test
    public void test_givenUnsortedQualitativeQuestion_whenMakeAnAnswer_withValidOption_thenReturnUnivaluedQualitativeAnswer() {
        // Arrange
        UnsortedQualitativeQuestion unsortedQualitativeQuestion = new UnsortedQualitativeQuestion();
        Option option = new Option("verd");

        // Act
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = unsortedQualitativeQuestion.makeAnAnswer(option);

        // Assert
        assertEquals("verd", univaluedQualitativeAnswer.getOption().getValue());
    }
}
