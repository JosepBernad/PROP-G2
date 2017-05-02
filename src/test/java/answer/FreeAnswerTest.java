package answer;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FreeAnswerTest {

    @Test
    public void test_whenParseSentence_withSentence_thenReturnsListWithLowercaseWords() {
        String sentence = "My name is Sergio";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"my", "name", "is", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_whenParseSentence_withSentenceWithDots_thenReturnsListWithoutDots() {
        String sentence = "My name is Sergio.";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"my", "name", "is", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

    @Test
    public void test_whenParseSentence_withSentenceWithCommas_thenReturnsListWithoutCommas() {
        String sentence = "My name, is Sergio,";

        List<String> words = FreeAnswer.parseSentence(sentence);

        String[] expectedWords = new String[]{"my", "name", "is", "sergio"};
        assertEquals(Arrays.asList(expectedWords), words);
    }

}
