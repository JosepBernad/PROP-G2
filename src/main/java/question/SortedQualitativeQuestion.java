package question;


import answer.Answer;
import answer.UnivaluedQualitativeAnswer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SortedQualitativeQuestion extends QualitativeQuestion {
    @Override
    public Answer makeAnAnswer() {
        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            univaluedQualitativeAnswer.setValue(new Option(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return univaluedQualitativeAnswer;
    }
}
