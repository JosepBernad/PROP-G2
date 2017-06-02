package question;

import answer.FreeAnswer;
import exceptions.InvalidSizeException;
import exceptions.NotInRangeException;

/**
 * Aquesta classe és una subclasse de Question i ens permet gestionar les preguntes del tipus
 FreeQuestion . Aquestes esperen un String de valor lliure com a valor de resposta
 *L'atribut de  la classeés el nombre màxim nombre de caràcters (allargada) de l’ String de la resposta
 */
public class FreeQuestion extends Question {

    private Integer maxSize;

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) throws NotInRangeException {
        if (maxSize <= 0) throw new NotInRangeException();
        this.maxSize = maxSize;
    }

    /**
     * Amb aquest mètode, a partir de la instància de FreeAnswer , crea una resposta. Abans de
     guardar-la dins persistència, ens comprovam que la mida de la resposta és més petita o
     igual que el maxSize, en cas contrari llança l’excepció InvalidSizeException
     * @param value
     * @return
     * @throws InvalidSizeException
     */
    public FreeAnswer makeAnAnswer(String value) throws InvalidSizeException {
        if (value.length() > maxSize) throw new InvalidSizeException();
        FreeAnswer freeAnswer = new FreeAnswer();
        if (value.isEmpty()) value = null;
        freeAnswer.setValue(value);
        return freeAnswer;
    }
}
