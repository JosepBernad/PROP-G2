package question;


import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

public abstract class QuestionBuilder {

    private TextField statement;

    public abstract Question build() throws NotInRangeException, EmptyRequiredAttributeException;

    public QuestionBuilder setStatement(TextField statement) {
        this.statement = statement;
        return this;
    }

    TextField getStatement() {
        return statement;
    }

    void isEmpty(TextField... textFields) throws EmptyRequiredAttributeException {
        for (TextField textField : textFields)
            if (textField.getText().isEmpty()) throw new EmptyRequiredAttributeException();
    }
}