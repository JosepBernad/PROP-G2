package question;

import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Map<TextField, TextField> options;

    public SortedQualitativeQuestion build() throws EmptyRequiredAttributeException, NotInRangeException {
        isEmpty(getStatement());
        SortedQualitativeQuestion question = new SortedQualitativeQuestion();
        question.setStatement(getStatement().getText());
        Set<Option> optionsSet = new HashSet<>();
        for (TextField textField : options.keySet())
            try {
                optionsSet.add(new Option(options.get(textField).getText(), Integer.parseInt(textField.getText())));
            } catch (NumberFormatException e) {
                throw new NotInRangeException();
            }
        question.setOptions(optionsSet);
        return question;
    }

    public SortedQualitativeQuestionBuilder setOptions(Map<TextField, TextField> options) {
        this.options = options;
        return this;
    }
}
