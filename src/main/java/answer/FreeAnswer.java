package answer;

import analysis.DistanceCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FreeAnswer extends Answer {

    private String value;

    public static FreeAnswer calculateCentroid(List<FreeAnswer> answers) {
        Map<String, Integer> occurrences = new HashMap<>();

        for (FreeAnswer answer : answers) {
            if (answer.getValue() != null) {
                List<String> strings = parseSentence(answer.getValue());
                for (String s : strings) {
                    if (!occurrences.containsKey(s)) occurrences.put(s, 0);
                    occurrences.put(s, occurrences.get(s) + 1);
                }
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

    static List<String> parseSentence(String s) {
        String[] parsed = s.replaceAll("[.,]", "").toLowerCase().split("\\s+");
        List<String> strings = new ArrayList<>(Arrays.asList(parsed));
        Set<String> functionalWords = getFunctionalWords();
        strings.removeAll(functionalWords);
        return strings;
    }

    private static Set<String> getFunctionalWords() {
        Set<String> strings = new HashSet<>();
        strings.addAll(getWordsFromFile("functional_words/empty.eng"));
        strings.addAll(getWordsFromFile("functional_words/empty.sp"));
        strings.addAll(getWordsFromFile("functional_words/empty.cat"));
        return strings;
    }

    private static Set<String> getWordsFromFile(String filename) {
        Set<String> strings = new HashSet<>();
        ClassLoader classLoader = FreeAnswer.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        Scanner s = null;
        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (s.hasNext())
            strings.add(s.next());
        s.close();
        return strings;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        String answerValue = ((FreeAnswer) answer).getValue();
        if (this.getValue() == null || answerValue == null) return 1D;
        return DistanceCalculator.calculateFreeQuestion(this.getValue(), answerValue);
    }
}
