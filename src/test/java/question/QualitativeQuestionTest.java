package question;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QualitativeQuestionTest {

    @Test
    public void givenQualitativeQuestion_whenAddNewOption_withValidOption_thenAddQuestion() {
        // Arrange
        QualitativeQuestion qualitativeQuestion = new SortedQualitativeQuestion();
        Option option = new Option("value", 1);

        // Act
        qualitativeQuestion.addOption(option);

        // Assert
        assertTrue(qualitativeQuestion.getOptions().contains(option));
    }
}
