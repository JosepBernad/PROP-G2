package question;


import answer.UnivaluedQualitativeAnswer;
import exceptions.NotValidOptionException;

public class UnsortedQualitativeQuestion extends QualitativeQuestion {

    public UnivaluedQualitativeAnswer makeAnAnswer(Option option) throws NotValidOptionException {
        if (!getOptions().contains(option)) throw new NotValidOptionException();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(option);
        return univaluedQualitativeAnswer;
    }
}
