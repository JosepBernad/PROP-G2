package question;

import exceptions.EmptyRequiredAttributeException;
import javafx.scene.control.TextField;

import java.util.Set;

public class MultivaluedUnsortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<Option> options;
    private TextField maxAnswers;

    public MultivaluedUnsortedQualitativeQuestion build() throws EmptyRequiredAttributeException {
        isEmpty(maxAnswers);
        MultivaluedUnsortedQualitativeQuestion question = new MultivaluedUnsortedQualitativeQuestion();
        question.setOptions(options);
        question.setnMaxAnswers(Integer.parseInt(maxAnswers.getText()));
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
