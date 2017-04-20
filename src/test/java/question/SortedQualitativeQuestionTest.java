package question;

import answer.UnivaluedQualitativeAnswer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortedQualitativeQuestionTest {

    @Test
    public void test_givenSortedQualitativeQuestion_whenMakeAnAnswer_withValidOption_thenReturnUnivaluedQualitativeAnswer() {
        // Arrange
        SortedQualitativeQuestion unsortedQualitativeQuestion = new SortedQualitativeQuestion();
        Option option = new Option("verd", 2);

        // Act
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = unsortedQualitativeQuestion.makeAnAnswer(option);

        // Assert
        Option expectedOption = new Option("verd", 2);
        assertEquals(expectedOption, univaluedQualitativeAnswer.getOption());
    }
}
