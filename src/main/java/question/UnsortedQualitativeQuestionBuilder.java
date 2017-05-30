package question;

import java.util.Set;

public class UnsortedQualitativeQuestionBuilder extends QuestionBuilder {

    private Set<Option> options;

    public UnsortedQualitativeQuestion build() {
        UnsortedQualitativeQuestion unsortedQualitativeQuestion = new UnsortedQualitativeQuestion();
        unsortedQualitativeQuestion.setOptions(options);
        return unsortedQualitativeQuestion;
    }

    public UnsortedQualitativeQuestionBuilder setOptions(Set<Option> options) {
        this.options = options;
        return this;
    }
}
