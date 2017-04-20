package question;

import answer.MultivaluedQualitativeAnswer;
import exceptions.TooMuchOptionsException;

import java.util.Set;

public class MultivaluedUnsortedQualitativeQuestion extends QualitativeQuestion {
    private Integer nMaxAnswers;

    public Integer getnMaxAnswers() {
        return nMaxAnswers;
    }

    public void setnMaxAnswers(Integer nMaxAnswers) {
        this.nMaxAnswers = nMaxAnswers;
    }

    public MultivaluedQualitativeAnswer makeAnAnswer(Set<Option> options) throws TooMuchOptionsException {
        if (options.size() > nMaxAnswers) throw new TooMuchOptionsException();
        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer.setOptions(options);
        return multivaluedQualitativeAnswer;
    }
}
