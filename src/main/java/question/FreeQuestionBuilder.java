package question;

import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

public class FreeQuestionBuilder extends QuestionBuilder {

    private TextField maxLenght;

    public FreeQuestion build() {
        FreeQuestion freeQuestion = new FreeQuestion();
        try {
            freeQuestion.setMaxSize(Integer.parseInt(maxLenght.getText()));
        } catch (NotInRangeException e) {
            e.printStackTrace();
        }
        freeQuestion.setStatement(getStatement().getText());
        return freeQuestion;
    }

    public FreeQuestionBuilder setMaxLenght(TextField maxLenght) {
        this.maxLenght = maxLenght;
        return this;
    }
}
