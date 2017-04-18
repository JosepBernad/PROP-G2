package answer;

import utils.FileUtils;

import java.util.Set;

public class FreeAnswer extends Answer {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void save() {
        Set<Answer> answers = getAnswers();
        answers.add(this);
        FileUtils.saveObjectInFile(answers, ANSWERS);
    }
}
