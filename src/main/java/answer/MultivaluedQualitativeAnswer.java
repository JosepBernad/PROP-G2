package answer;

import analysis.DistanceCalculator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    @JsonIgnore
    private Set<String> getValues() {
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
