package answer;

import analysis.DistanceCalculator;
import question.NumericQuestion;
import survey.Survey;

import java.util.List;

/**
 * Aquesta classe és una subclasse de Answer i ens permet gestionar la resposta d’un usuari
 a una pregunta de tipus numèrica d’una enquesta determinada
 *Per aquesta classe disposem de tres atributs (a part dels de la seva superclasse), un valor
 on guardem la resposta de l’usuari i un min i un max que és on emmagatzemem els valors
 llindars que accepta la pregunta en qüestió
 */
public class NumericAnswer extends Answer {

    private Double value;

    /**
     * Aquest mètode calcula el centroide d'un conjunt de respostes de tipus NumericAnswer
     * @param answers
     * @return
     */
    public static NumericAnswer calculateCentroid(List<NumericAnswer> answers) {
        Double sum = 0D;
        for (NumericAnswer answer : answers)
            sum += answer.getValue();

        NumericAnswer numericAnswer = new NumericAnswer();
        numericAnswer.setValue(sum / answers.size());
        return numericAnswer;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Aquest mètode retorna la distància (entre 0 i 1) entre una resposta i una altre resposta del
     mateix tipus.
     * @param answer
     * @return
     */
    @Override
    public Double calculateDistance(Answer answer) {
        NumericQuestion question = (NumericQuestion) Survey.getSurveyById(getSurveyId()).getQuestion(getQuestionId());
        return DistanceCalculator.calculateNumeric(getValue(), ((NumericAnswer) answer).getValue(), question.getMax(), question.getMin());
    }

}
