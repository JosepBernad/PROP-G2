package question;

import exceptions.RepeatedOptionWeightException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void test_givenQualitativeQuestion_whenSetOptionsValuesOrderedByWeight_thenReturnSortedList()
    {
        // Arrange
        Set<Option> options = createSet(new Option("Poc", 0), new Option("Mitja", 1), new Option("Molt", 2));
        QualitativeQuestion qualitativeQuestion = new SortedQualitativeQuestion();
        qualitativeQuestion.setOptions(options);

        // Act
        List<String> returnedList = qualitativeQuestion.optionsValuesOrderedByWeight();

        // Assert
        List<String> expectedList = createList("Poc", "Mitja", "Molt");
        assertEquals(expectedList,returnedList);
    }

    private Set<Option> createSet(Option... options) {
        Set<Option> set = new HashSet<>();
        set.addAll(Arrays.asList(options));
        return set;
    }

    private List<String> createList(String... value) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(value));
        return list;
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

    @Test
    public void test_givenQualitativeQuestion_whenAddOptio_withoutWeight_thenAddItToTheOptionSet() throws RepeatedOptionWeightException {
        // Arrange
        UnsortedQualitativeQuestion unsortedQualitativeQuestion = new UnsortedQualitativeQuestion();
        Option option = new Option("option");
        Option option2 = new Option("option");

        // Act
        unsortedQualitativeQuestion.addOption(option);
        unsortedQualitativeQuestion.addOption(option2);

        // Assert
        assertTrue(unsortedQualitativeQuestion.getOptions().contains(option));
        assertTrue(unsortedQualitativeQuestion.getOptions().contains(option2));

    }
}
