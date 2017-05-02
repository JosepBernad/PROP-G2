package question;

import answer.FreeAnswer;
import exceptions.InvalidSizeException;
import exceptions.NotInRangeException;

public class FreeQuestion extends Question {

    private Integer maxSize;

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) throws NotInRangeException {
        if (maxSize <= 0) throw new NotInRangeException();
        this.maxSize = maxSize;
    }

    public FreeAnswer makeAnAnswer(String value) throws InvalidSizeException {
        if (value.length() > maxSize) throw new InvalidSizeException();
        FreeAnswer freeAnswer = new FreeAnswer();
        if (value.isEmpty()) value = null;
        freeAnswer.setValue(value);
        return freeAnswer;
    }
}
