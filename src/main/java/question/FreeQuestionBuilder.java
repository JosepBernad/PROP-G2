package question;

import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

public class FreeQuestionBuilder extends QuestionBuilder {

    private TextField maxLength;

    public FreeQuestion build() throws NotInRangeException, EmptyRequiredAttributeException {
        isEmpty(maxLength, getStatement());
        FreeQuestion freeQuestion = new FreeQuestion();
        try {
            freeQuestion.setMaxSize(Integer.parseInt(maxLength.getText()));
        } catch (NumberFormatException e) {
            throw new NotInRangeException();
        }
        freeQuestion.setStatement(getStatement().getText());
        return freeQuestion;
    }

    public FreeQuestionBuilder setMaxLength(TextField maxLength) {
        this.maxLength = maxLength;
        return this;
    }
}
