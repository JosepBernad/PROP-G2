package question;

import answer.MultivaluedQualitativeAnswer;
import exceptions.TooMuchOptionsException;

import java.util.Set;

/**
 *Aquesta classe és una subclasse de QualitativeQuestion que és subclasse de Question que
 ens permet gestionar les preguntes del tipus MultivaluedUnsortedQualitativeQuestion .
 Aquestes esperen un conjunt de n Opcions tal que n més petit o igual a nMaxAnswers
 L'atribut de la classe serveix per determinar el nombre màxim d’opcions que l’usuari pot elegir com
 a resposta
 */
public class MultivaluedUnsortedQualitativeQuestion extends QualitativeQuestion {
    private Integer nMaxAnswers;

    public Integer getnMaxAnswers() {
        return nMaxAnswers;
    }

    public void setnMaxAnswers(Integer nMaxAnswers) {
        this.nMaxAnswers = nMaxAnswers;
    }

    /**
     * Amb aquest mètode, a partir de la instància de MultivaluedQualitativeAnswer , crea una
     resposta (dins un set d'opcions, ja que poden ser més d’una)
     * @param options les ocpions
     * @return una multivaluedQualitativeAnswer
     * @throws TooMuchOptionsException excepció
     */
    public MultivaluedQualitativeAnswer makeAnAnswer(Set<Option> options) throws TooMuchOptionsException {
        if (options.size() > nMaxAnswers) throw new TooMuchOptionsException();
        MultivaluedQualitativeAnswer multivaluedQualitativeAnswer = new MultivaluedQualitativeAnswer();
        multivaluedQualitativeAnswer.setOptions(options);
        return multivaluedQualitativeAnswer;
    }
}
