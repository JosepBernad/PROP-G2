package answer;

import analysis.DistanceCalculator;
import question.Option;
import question.UnsortedQualitativeQuestion;

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
        return DistanceCalculator.calculateUnsortedUnivaluedQualitative(option.getValue(), ((UnivaluedQualitativeAnswer) answer).getOption().getValue());
    }

}
