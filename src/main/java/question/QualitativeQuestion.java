package question;


import exceptions.RepeatedOptionWeightException;

import java.util.HashSet;
import java.util.Set;

public abstract class QualitativeQuestion extends Question {
    private Set<Option> options = new HashSet<>();

    public Set<Option> getOptions() {
        return options;
    }

    public void addOption(Option option) throws RepeatedOptionWeightException {
        for (Option opt: options)
            if (opt.getWeight().equals(option.getWeight()))
                throw new RepeatedOptionWeightException();
        options.add(option);
    }
}
