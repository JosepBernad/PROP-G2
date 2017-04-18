package answer;

import question.Option;

import java.util.Set;

public class MultivaluedQualitativeAnswer extends QualitativeAnswer {

    private Set<Option> options;

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

}
