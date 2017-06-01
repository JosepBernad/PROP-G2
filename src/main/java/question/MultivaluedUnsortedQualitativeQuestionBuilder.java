package question;

import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Set;

public class MultivaluedUnsortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<TextField> options;
    private TextField maxAnswers;

    public MultivaluedUnsortedQualitativeQuestion build() throws EmptyRequiredAttributeException, NotInRangeException {
        isEmpty(maxAnswers, getStatement());
        int nMaxAnswers;
        try {
            nMaxAnswers = Integer.parseInt(maxAnswers.getText());
        } catch (NumberFormatException e) {
            throw new NotInRangeException();
        }
        if (nMaxAnswers > options.size()) throw new NotInRangeException();
        MultivaluedUnsortedQualitativeQuestion question = new MultivaluedUnsortedQualitativeQuestion();
        question.setStatement(getStatement().getText());
        Set<Option> optionsSet = new HashSet<>();
        for (TextField textField : this.options)
            optionsSet.add(new Option(textField.getText()));
        question.setOptions(optionsSet);
        question.setnMaxAnswers(nMaxAnswers);
        return question;
    }

    public MultivaluedUnsortedQualitativeQuestionBuilder setOptions(Set<TextField> options) {
        this.options = options;
        return this;
    }

    public MultivaluedUnsortedQualitativeQuestionBuilder setMaxAnswers(TextField maxAnswers) {
        this.maxAnswers = maxAnswers;
        return this;
    }
}
