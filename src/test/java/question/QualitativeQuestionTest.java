package question;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void test_givenQualitativeQuestion_whenSetOptionsValuesOrderedByWeight_thenReturnSortedList()
    {
        // Arrange
        Set<Option> options = createSet(new Option("Poc", 0), new Option("Mitja", 1), new Option("Molt", 2));
        QualitativeQuestion qualitativeQuestion = new SortedQualitativeQuestion();
        qualitativeQuestion.setOptions(options);

        // Act
        List<String> returnedList = qualitativeQuestion.getOptionsValuesOrderedByWeight();

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
}
