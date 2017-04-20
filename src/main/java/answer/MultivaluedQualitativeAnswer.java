package answer;

import analysis.DistanceCalculator;
import question.Option;

import java.util.HashSet;
import java.util.Set;

public class MultivaluedQualitativeAnswer extends QualitativeAnswer {

    private Set<Option> options;

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateUnsortedMultivaluedQualitative(getValues(), ((MultivaluedQualitativeAnswer) answer).getValues());
    }

    private Set<String> getValues() {
        Set<String> values = new HashSet<>();
        for (Option option : options)
            values.add(option.getValue());
        return values;
    }
}
