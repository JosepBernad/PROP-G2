package answer;

import analysis.DistanceCalculator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import question.Option;

import java.util.*;

/**
 * Aquesta classe és una subclasse de QualitativeAnswer i ens permet gestionar la resposta
 d’un usuari a una pregunta de tipus multivaluada d’una enquesta determinada
 *
 * Per aquesta classe disposem d’un atribut (a part dels de la seva superclasse), un set on
 emmagatzemem totes les opcions que ha respost l’usuari
 */
public class MultivaluedQualitativeAnswer extends QualitativeAnswer {

    private Set<Option> options;

    /**
     * Aquest mètode calcula el centroide d'un conjunt de respostes de tipus MultivaluedQualitativeAnswer
     * @param answers són les respostes
     * @return una multivaluedQualitativeAnswer
     */
    public static MultivaluedQualitativeAnswer calculateCentroid(List<MultivaluedQualitativeAnswer> answers) {
        Map<Option, Integer> occurrences = new HashMap<>();

        for (MultivaluedQualitativeAnswer answer : answers) {
            Set<Option> options = answer.getOptions();
            for (Option value : options) {
                if (!occurrences.containsKey(value)) occurrences.put(value, 0);
                occurrences.put(value, occurrences.get(value) + 1);
            }
        }

        Set<Option> mode = new HashSet<>();
        Integer max = 0;

        for (Option option : occurrences.keySet()) {
            Integer integer = occurrences.get(option);
            if (integer.equals(max)) mode.add(option);
            else if (integer > max) {
                mode.clear();
                mode.add(option);
                max = integer;
            }
        }

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer.setOptions(mode);
        return multivaluedQualitativeAnswer;
    }

    public Set<Option> getOptions() {
        return options;
    }

    /**
     * Amb el getter aconseguim obtenir el valor de l’atribut privat de la classe, i amb els setter el
     que fem és poder donar valor a aquest fora de la classe
     * @param options són les opcions
     */
    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    /**
     * Aquest mètode privat retorna tots els valors de les opcions de la classe instanciada
     * @return un set d'strings
     */
    @JsonIgnore
    private Set<String> getValues() {
        Set<String> values = new HashSet<>();
        for (Option option : options)
            values.add(option.getValue());
        return values;
    }

    /**
     * Aquest mètode retorna la distància (entre 0 i 1) entre una resposta i una altre resposta del
     mateix tipus
     * @param answer són les respostes
     * @return un double
     */
    @Override
    public Double calculateDistance(Answer answer) {
        Set<String> answerValues = ((MultivaluedQualitativeAnswer) answer).getValues();
        if (getValues().isEmpty() || answerValues.isEmpty()) return 1D;
        return DistanceCalculator.calculateUnsortedMultivaluedQualitative(this.getValues(), answerValues);
    }
}
