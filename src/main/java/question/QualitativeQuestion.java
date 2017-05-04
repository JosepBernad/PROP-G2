package question;


import answer.UnivaluedQualitativeAnswer;
import exceptions.NotValidOptionException;
import exceptions.RepeatedOptionWeightException;

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

    public void addOption(Option option) throws RepeatedOptionWeightException {
        if (option.getWeight() != null)
            for (Option opt : options)
                if (opt.getWeight().equals(option.getWeight()))
                    throw new RepeatedOptionWeightException();
        options.add(option);
    }

    public List<String> optionsValuesOrderedByWeight() {
        String[] values = new String[options.size()];
        for (Option option : options) {
            values[option.getWeight()] = option.getValue();
        }
        return Arrays.asList(values);
    }

    public UnivaluedQualitativeAnswer makeAnAnswer(Option option) throws NotValidOptionException {
        if (!getOptions().contains(option)) throw new NotValidOptionException();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(option);
        return univaluedQualitativeAnswer;
    }
}
