package question;

import exceptions.RepeatedOptionWeightException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QualitativeQuestionTest {

    @Test
    public void test_givenQualitativeQuestion_whenAddOption_withValidOption_thenAddQuestion() throws RepeatedOptionWeightException {
        // Arrange
        QualitativeQuestion qualitativeQuestion = new SortedQualitativeQuestion();
        Option option = new Option("value", 1);

        // Act
        qualitativeQuestion.addOption(option);

        // Assert
        assertTrue(qualitativeQuestion.getOptions().contains(option));
    }

    @Test(expected = RepeatedOptionWeightException.class)
    public void test_givenQualitativeQuestion_whenAddOption_withRepeatedWeight_thenThrowsRepeatedOptionWeightException() throws RepeatedOptionWeightException {
        // Arrange
        QualitativeQuestion qualitativeQuestion = new SortedQualitativeQuestion();
        Option option1 = new Option("option1",1);
        Option option2 = new Option("option2",1);

        // Act
        qualitativeQuestion.addOption(option1);
        qualitativeQuestion.addOption(option2);
    }
}
