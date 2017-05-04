package answer;

import analysis.DistanceCalculator;
import question.Option;

import java.util.*;

public class MultivaluedQualitativeAnswer extends QualitativeAnswer {

    private Set<Option> options;

    public static MultivaluedQualitativeAnswer calculateCentroid(List<MultivaluedQualitativeAnswer> answers) {
        Map<Option, Integer> occurrences = new HashMap<>();

        for (MultivaluedQualitativeAnswer answer : answers) {
            Set<Option> options = answer.getOptions();
            for (Option value : options) {
                if (!occurrences.containsKey(value)) occurrences.put(value, 0);
                occurrences.put(value, occurrences.get(value) + 1);
            }
        }

        Option mode = null;
        Integer max = 0;

        for (Option option : occurrences.keySet()) {
            Integer integer = occurrences.get(option);
            if (integer > max) {
                mode = option;
                max = integer;
            }
        }

        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        Set<Option> options = new HashSet<>();
        options.add(mode);
        multivaluedQualitativeAnswer.setOptions(options);
        return multivaluedQualitativeAnswer;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Set<String> getValues() {
        Set<String> values = new HashSet<>();
        for (Option option : options)
            values.add(option.getValue());
        return values;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateUnsortedMultivaluedQualitative(this.getValues(), ((MultivaluedQualitativeAnswer) answer).getValues());
    }
}
