package question;


import answer.UnivaluedQualitativeAnswer;

public class UnsortedQualitativeQuestion extends QualitativeQuestion {

    public UnivaluedQualitativeAnswer makeAnAnswer(Option option) {
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(option);
        return univaluedQualitativeAnswer;
    }
}
