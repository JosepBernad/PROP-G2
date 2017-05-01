package answer;

import analysis.DistanceCalculator;
import question.Option;

public class UnivaluedQualitativeAnswer extends QualitativeAnswer {

    private Option option;

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
