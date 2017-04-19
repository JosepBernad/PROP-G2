package question;

import answer.Answer;
import answer.MultivaluedQualitativeAnswer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class MultivaluedUnsortedQualitativeQuestion extends UnsortedQualitativeQuestion {
    private Integer nMaxAnswers;

    public Integer getnMaxAnswers() {
        return nMaxAnswers;
    }

    public void setnMaxAnswers(Integer nMaxAnswers) {
        this.nMaxAnswers = nMaxAnswers;
    }

    @Override
    public Answer makeAnAnswer() {
        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Set<Option> options = new HashSet<>();
            options.add(new Option(br.readLine()));
            multivaluedQualitativeAnswer.setOptions(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multivaluedQualitativeAnswer;
    }
}
