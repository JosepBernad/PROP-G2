package answer;

import analysis.DistanceCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Aquesta classe és una subclasse de Answer i ens permet gestionar la resposta d’un usuari a
 una pregunta de tipus lliure d’una enquesta determinada
 Per aquesta classe disposem d’un atribut (a part dels de la seva superclasse), el valor que
 ens guarda la resposta de l’usuari a certa d’aquest tipus
 *
 */
public class FreeAnswer extends Answer {

    private String value;

    /**
     * Aquest mètode calcula el centroide d'un conjunt de respostes de tipus lliure
     * @param answers són les respostes
     * @return una llista de freeAnswers
     */
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

    /**
     * Aquest mètode transforma una paraula en una llista de paraules
     * @param s és la paraula
     * @return una llista d'strings
     */
    static List<String> parseSentence(String s) {
        String[] parsed = s.replaceAll("[.,]", "").toLowerCase().split("\\s+");
        List<String> strings = new ArrayList<>(Arrays.asList(parsed));
        Set<String> functionalWords = getFunctionalWords();
        strings.removeAll(functionalWords);
        return strings;
    }

    /**
     * Aquest mètode obté les paraules funcionals
     * @return un set d'strings
     */
    private static Set<String> getFunctionalWords() {
        Set<String> strings = new HashSet<>();
        strings.addAll(getWordsFromFile("functional_words/empty.eng"));
        strings.addAll(getWordsFromFile("functional_words/empty.sp"));
        strings.addAll(getWordsFromFile("functional_words/empty.cat"));
        return strings;
    }

    /**
     * Aquesta mètode llegeix d'un fitxer i et retorna el conjunt de les paraules que el formen
     * @param filename és el fitxer
     * @return un set d'strings
     */
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

    /**
     * Aquest mètode retorna la distància (entre 0 i 1) entre una resposta i una altre resposta del
     mateix tipus.
     * @param answer és la resposta
     * @return un double
     */
    @Override
    public Double calculateDistance(Answer answer) {
        String answerValue = ((FreeAnswer) answer).getValue();
        if (this.getValue() == null || answerValue == null) return 1D;
        return DistanceCalculator.calculateFreeQuestion(this.getValue(), answerValue);
    }
}
