package question;

import answer.MultivaluedQualitativeAnswer;
import answer.UnivaluedQualitativeAnswer;
import exceptions.TooMuchOptionsException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MultivaluedUnsortedQualitativeQuestionTest {

    @Test
    public void test_givenMultivaluedUnsortedQualitativeQuestion_whenMakeAnAnswer_withValidOption_thenReturnMultivaluedQualitativeAnswer() throws TooMuchOptionsException {
        // Arrange
        MultivaluedUnsortedQualitativeQuestion multivaluedUnsortedQualitativeQuestion = new MultivaluedUnsortedQualitativeQuestion();
        multivaluedUnsortedQualitativeQuestion.setnMaxAnswers(4);
        Set<Option> options = createSet(new Option("verd"), new Option("blau"), new Option("groc"));

        // Act
        MultivaluedQualitativeAnswer univaluedQualitativeAnswer = multivaluedUnsortedQualitativeQuestion.makeAnAnswer(options);

        // Assert
        Set<Option> expectedOptions = createSet(new Option("verd"), new Option("blau"), new Option("groc"));
        assertEquals(expectedOptions, univaluedQualitativeAnswer.getOptions());
    }

    @Test(expected = TooMuchOptionsException.class)
    public void test_givenMultivaluedUnsortedQualitativeQuestion_whenMakeAnAnswer_withMoreOptionsThanAllowed_thenThrowsTooMuchOptionsException() throws TooMuchOptionsException {
        // Arrange
        MultivaluedUnsortedQualitativeQuestion multivaluedUnsortedQualitativeQuestion = new MultivaluedUnsortedQualitativeQuestion();
        multivaluedUnsortedQualitativeQuestion.setnMaxAnswers(2);
        Set<Option> options = createSet(new Option("verd"), new Option("blau"), new Option("groc"));

        // Act
        multivaluedUnsortedQualitativeQuestion.makeAnAnswer(options);
    }

    private Set<Option> createSet(Option... options) {
        Set<Option> set = new HashSet<>();
        set.addAll(Arrays.asList(options));
        return set;
    }
}
