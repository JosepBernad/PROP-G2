package question;


import javafx.scene.control.TextField;

public abstract class QuestionBuilder {

    private TextField statement;

    public abstract Question build();

    public QuestionBuilder setStatement(TextField statement) {
        this.statement = statement;
        return this;
    }

    TextField getStatement() {
        return statement;
    }
}