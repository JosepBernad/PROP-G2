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
     * @param answers
     * @return
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
     * @param options
     */
    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    /**
     * Aquest mètode privat retorna tots els valors de les opcions de la classe instanciada
     * @return
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
     * @param answer
     * @return
     */
    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateUnsortedMultivaluedQualitative(this.getValues(), ((MultivaluedQualitativeAnswer) answer).getValues());
    }
}
