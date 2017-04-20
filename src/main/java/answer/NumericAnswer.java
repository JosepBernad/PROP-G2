package answer;

import analysis.DistanceCalculator;

public class NumericAnswer extends Answer {

    private Integer value;
    private Integer max;
    private Integer min;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMax() {
        return max;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMin() {
        return min;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateNumeric(value, ((NumericAnswer) answer).getValue(),max,min);
    }

}
