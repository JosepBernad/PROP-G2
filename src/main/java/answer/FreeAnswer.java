package answer;

import analysis.DistanceCalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeAnswer extends Answer {

    private String value;

    public static FreeAnswer calculateCentroid(List<FreeAnswer> answers) {
        Map<String, Integer> occurrences = new HashMap<>();

        for (FreeAnswer answer : answers) {
            List<String> strings = parseSentence(answer.getValue());
            for (String s : strings) {
                if (!occurrences.containsKey(s)) occurrences.put(s, 0);
                occurrences.put(s, occurrences.get(s) + 1);
            }
        }

        String mode = null;
        Integer max = 0;

        for (String s : occurrences.keySet()) {
            Integer integer = occurrences.get(s);
            if (integer > max) {
                mode = s;
                max = integer;
            }
        }

        FreeAnswer freeAnswer = new FreeAnswer();
        freeAnswer.setValue(mode);
        return freeAnswer;
    }

    // TODO: discard functional words
    static List<String> parseSentence(String s) {
        String[] words = s.replaceAll("[.]", "").toLowerCase().split("\\s+");
        return Arrays.asList(words);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateDistance(this, (FreeAnswer) answer);
    }
}
