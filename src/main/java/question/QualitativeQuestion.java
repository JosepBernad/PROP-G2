package question;


import answer.UnivaluedQualitativeAnswer;
import exceptions.NotValidOptionException;
import exceptions.RepeatedOptionWeightException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Aquesta classe és una subclasse abstract de Question que ens permet gestionar les
 preguntes del tipus QualitativeQuestion (les quals s’identifiquen per tenir un conjunt de
 possible respostes)
 L'atribut de la classe és el conjunt de possible opcions que, depenent de la subclasse, l’usuari pot triar com a
 resposta
 */
public abstract class QualitativeQuestion extends Question {
    private Set<Option> options = new HashSet<>();

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    /**
     * Mètode que ens serveix per afegir una opció al set d'opcions de la instància
     * @param option l'opció
     * @throws RepeatedOptionWeightException excepció
     */
    public void addOption(Option option) throws RepeatedOptionWeightException {
        if (option.getWeight() != null)
            for (Option opt : options)
                if (opt.getWeight().equals(option.getWeight()))
                    throw new RepeatedOptionWeightException();
        options.add(option);
    }

    /**
     * Mètode que retorna una llista d’ String dels valors de les opcions ordenada pels pesos
     d’aquestes opcions
     * @return una llista d'strings
     */
    public List<String> optionsValuesOrderedByWeight() {
        String[] values = new String[options.size()];
        for (Option option : options) {
            values[option.getWeight()] = option.getValue();
        }
        return Arrays.asList(values);
    }

    /**
     * Amb aquest mètode, a partir de la instància de QualitativeQuestion , crea una resposta passant per
     paràmetre el valor de l'opció que l’usuari ha introduit
     * @param option l'opció
     * @return una univaluedQualitativeAnswer
     * @throws NotValidOptionException excepció
     */
    public UnivaluedQualitativeAnswer makeAnAnswer(Option option) throws NotValidOptionException {
        if (!getOptions().contains(option)) throw new NotValidOptionException();

        UnivaluedQualitativeAnswer univaluedQualitativeAnswer = new UnivaluedQualitativeAnswer();
        univaluedQualitativeAnswer.setOption(option);
        return univaluedQualitativeAnswer;
    }
}
