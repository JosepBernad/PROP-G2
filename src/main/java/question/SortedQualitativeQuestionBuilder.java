package question;

import exceptions.EmptyRequiredAttributeException;

import java.util.Set;

public class SortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<Option> options;

    public SortedQualitativeQuestion build() throws EmptyRequiredAttributeException {
        isEmpty(getStatement());
        SortedQualitativeQuestion sortedQualitativeQuestion = new SortedQualitativeQuestion();
        sortedQualitativeQuestion.setOptions(options);
        return sortedQualitativeQuestion;
    }

    public SortedQualitativeQuestionBuilder setOptions(Set<Option> options) {
        this.options = options;
        return this;
    }
}
