package question;

import exceptions.EmptyRequiredAttributeException;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Set;

public class UnsortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<TextField> options;

    public UnsortedQualitativeQuestion build() throws EmptyRequiredAttributeException {
        isEmpty(getStatement());
        UnsortedQualitativeQuestion question = new UnsortedQualitativeQuestion();
        question.setStatement(getStatement().getText());
        Set<Option> optionsSet = new HashSet<>();
        for (TextField textField : this.options)
            optionsSet.add(new Option(textField.getText()));
        question.setOptions(optionsSet);
        return question;
    }

    public UnsortedQualitativeQuestionBuilder setOptions(Set<TextField> options) {
        this.options = options;
        return this;
    }
}
