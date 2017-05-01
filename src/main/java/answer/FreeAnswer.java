package answer;

import analysis.DistanceCalculator;

public class FreeAnswer extends Answer {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateDistance(this, (FreeAnswer) answer);
    }
}
