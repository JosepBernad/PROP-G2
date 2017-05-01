package question;

import answer.NumericAnswer;
import exceptions.NotInRangeException;

public class NumericQuestion extends Question {

    private Integer min;
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public NumericAnswer makeAnAnswer(Double value) throws NotInRangeException {
        if (value < min || value > max) throw new NotInRangeException();
        NumericAnswer numericAnswer = new NumericAnswer();
        numericAnswer.setValue(value);
        return numericAnswer;
    }
}
