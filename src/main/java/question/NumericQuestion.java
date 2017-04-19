package question;

import answer.Answer;
import answer.NumericAnswer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    @Override
    public Answer makeAnAnswer() {
        NumericAnswer numericAnswer = new NumericAnswer();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            numericAnswer.setValue(br.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numericAnswer;
    }
}
