package question;

import answer.NumericAnswer;
import exceptions.NotInRangeException;

/**
 * Aquesta classe és una subclasse de Question que ens permet gestionar les preguntes del
 tipus NumericQuestion . Aquestes esperen un valor en format Integer
 Els mètodes determinen els valors mínim i màxim de la resposta
 */
public class NumericQuestion extends Question {

    private Integer min;
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) throws NotInRangeException {
        if (max != null && min > max) throw new NotInRangeException();
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) throws NotInRangeException {
        if (min != null && max < min) throw new NotInRangeException();
        this.max = max;
    }

    /**
     * Amb aquest mètode, a partir de la instància de NumericAnswer , crea una resposta passant per
     paràmetre el valor del número que l’usuari ha introduit
     * @param value
     * @return
     * @throws NotInRangeException
     */
    public NumericAnswer makeAnAnswer(Double value) throws NotInRangeException {
        if (value < min || value > max) throw new NotInRangeException();
        NumericAnswer numericAnswer = new NumericAnswer();
        numericAnswer.setQuestionId(getId());
        numericAnswer.setValue(value);
        return numericAnswer;
    }
}
