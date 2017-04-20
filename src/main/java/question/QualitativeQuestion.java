package question;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class QualitativeQuestion extends Question {
    private Set<Option> options = new HashSet<>();

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public List<String> getOptionsValuesOrderedByWeight() {
        String[] values = new String[options.size()];
        for (Option option : options) {
            values[option.getWeight()] = option.getValue();
        }
        return Arrays.asList(values);
    }
}
