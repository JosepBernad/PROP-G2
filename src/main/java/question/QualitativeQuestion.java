package question;


import java.util.HashSet;
import java.util.Set;

public abstract class QualitativeQuestion extends Question {
    private Set<Option> options = new HashSet<>();

    public Set<Option> getOptions() {
        return options;
    }

    // TODO: test it
    public void addOption(Option option) {
        options.add(option);
    }
}
