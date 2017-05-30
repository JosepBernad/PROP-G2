package question;


import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

public class NumericQuestionBuilder extends QuestionBuilder {

    private TextField minValue;
    private TextField maxValue;

    public NumericQuestion build() {
        NumericQuestion numericQuestion = new NumericQuestion();

        try {
            numericQuestion.setMax(Integer.parseInt(maxValue.getText()));
            numericQuestion.setMin(Integer.parseInt(minValue.getText()));
        } catch (NotInRangeException e) {
            e.printStackTrace();
        }
        numericQuestion.setStatement(getStatement().getText());
        return numericQuestion;
    }

    public NumericQuestionBuilder setMinValue(TextField minValue) {
        this.minValue = minValue;
        return this;
    }

    public NumericQuestionBuilder setMaxValue(TextField maxValue) {
        this.maxValue = maxValue;
        return this;
    }
}
