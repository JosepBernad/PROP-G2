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
     * @param answers són les respostes
     * @return una numericAnswer
     */
    public static NumericAnswer calculateCentroid(List<NumericAnswer> answers) {
        Double sum = null;
        Integer nullAnswers = 0;
        for (NumericAnswer answer : answers) {
            if (answer.getValue() != null) {
                if (sum == null) sum = 0D;
                sum += answer.getValue();
            } else ++nullAnswers;
        }

        NumericAnswer numericAnswer = new NumericAnswer();
        numericAnswer.setValue(sum == null ? null : sum / (answers.size() - nullAnswers));
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
     * @param answer és la resposta
     * @return un double
     */
    @Override
    public Double calculateDistance(Answer answer) {
        NumericQuestion question = (NumericQuestion) Survey.getSurveyById(getSurveyId()).getQuestion(getQuestionId());
        Double answerValue = ((NumericAnswer) answer).getValue();
        if (getValue() == null || answerValue == null) return 1D;
        return DistanceCalculator.calculateNumeric(getValue(), answerValue, question.getMax(), question.getMin());
    }

}
