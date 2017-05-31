package question;

import exceptions.EmptyRequiredAttributeException;
import exceptions.NotInRangeException;
import javafx.scene.control.TextField;

import java.util.Set;

public class MultivaluedUnsortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<Option> options;
    private TextField maxAnswers;

    public MultivaluedUnsortedQualitativeQuestion build() throws EmptyRequiredAttributeException, NotInRangeException {
        isEmpty(maxAnswers);
        int nMaxAnswers;
        try {
            nMaxAnswers = Integer.parseInt(maxAnswers.getText());
        } catch (NumberFormatException e) {
            throw new NotInRangeException();
        }
        if (nMaxAnswers < options.size()) throw new NotInRangeException();
        MultivaluedUnsortedQualitativeQuestion question = new MultivaluedUnsortedQualitativeQuestion();
        question.setOptions(options);
        question.setnMaxAnswers(nMaxAnswers);
        return question;
    }

    public MultivaluedUnsortedQualitativeQuestionBuilder setOptions(Set<Option> options) {
        this.options = options;
        return this;
    }

    public MultivaluedUnsortedQualitativeQuestionBuilder setMaxAnswers(TextField maxAnswers) {
        this.maxAnswers = maxAnswers;
        return this;
    }
}
