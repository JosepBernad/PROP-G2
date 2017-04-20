package question;


import answer.UnivaluedQualitativeAnswer;

public class SortedQualitativeQuestion extends QualitativeQuestion {
    public UnivaluedQualitativeAnswer makeAnAnswer(Option option) {
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(option);
        return univaluedQualitativeAnswer;
    }
}
