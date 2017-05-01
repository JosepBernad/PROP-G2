package answer;

import analysis.DistanceCalculator;
import question.Option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnivaluedQualitativeAnswer extends QualitativeAnswer {

    private Option option;

    public static UnivaluedQualitativeAnswer calculateCentroid(List<UnivaluedQualitativeAnswer> answers) {
        Map<Option, Integer> occurrences = new HashMap<>();

        for (UnivaluedQualitativeAnswer answer : answers) {
            Option value = answer.getOption();
            if (!occurrences.containsKey(value)) occurrences.put(value, 0);
            occurrences.put(value, occurrences.get(value) + 1);
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

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(mode);
        return univaluedQualitativeAnswer;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        if (option.getWeight() == null)
            return DistanceCalculator.calculateDistanceUnsorted(this, (UnivaluedQualitativeAnswer) answer);
        return DistanceCalculator.calculateDistanceSorted(this, (UnivaluedQualitativeAnswer) answer);
    }
}
