package answer;

import question.Option;
import utils.FileUtils;

import java.util.Set;

public class UnivaluedQualitativeAnswer extends QualitativeAnswer {

    private Option value;

    public Option getValue() {
        return value;
    }

    public void setValue(Option value) {
        this.value = value;
    }

    @Override
    public void save() {
        Set<Answer> answers = getAnswers();
        answers.add(this);
        FileUtils.saveObjectInFile(answers, ANSWERS);
    }
}
