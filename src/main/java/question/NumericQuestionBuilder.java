package question;


import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

public class NumericQuestionBuilder extends QuestionBuilder {

    private TextField minValue;
    private TextField maxValue;

    public NumericQuestion build() throws NotInRangeException, EmptyRequiredAttributeException {
        isEmpty(minValue, maxValue);
        NumericQuestion numericQuestion = new NumericQuestion();
        try {
            numericQuestion.setMin(Integer.parseInt(minValue.getText()));
            numericQuestion.setMax(Integer.parseInt(maxValue.getText()));
        } catch (NumberFormatException e) {
            throw new NotInRangeException();
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
