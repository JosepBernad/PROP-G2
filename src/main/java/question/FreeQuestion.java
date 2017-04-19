package question;

import answer.Answer;
import answer.FreeAnswer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FreeQuestion extends Question {

    private Integer maxSize;

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public Answer makeAnAnswer() {
        FreeAnswer freeAnswer = new FreeAnswer();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            freeAnswer.setValue(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freeAnswer;
    }
}
