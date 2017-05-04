package question;

import answer.NumericAnswer;
import exceptions.NotInRangeException;

public class NumericQuestion extends Question {

    private Integer min;
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) throws NotInRangeException {
        if (max != null && min > max) throw new NotInRangeException();
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) throws NotInRangeException {
        if (min != null && max < min) throw new NotInRangeException();
        this.max = max;
    }

    public NumericAnswer makeAnAnswer(Double value) throws NotInRangeException {
        if (value < min || value > max) throw new NotInRangeException();
        NumericAnswer numericAnswer = new NumericAnswer();
        numericAnswer.setQuestionId(getId());
        numericAnswer.setValue(value);
        return numericAnswer;
    }
}
