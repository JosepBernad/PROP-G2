package answer;

import analysis.DistanceCalculator;
import question.NumericQuestion;
import survey.Survey;

import java.util.List;

public class NumericAnswer extends Answer {

    private Double value;

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

    @Override
    public Double calculateDistance(Answer answer) {
        NumericQuestion question = (NumericQuestion) Survey.getSurveyById(getSurveyId()).getQuestion(getQuestionId());
        Double answerValue = ((NumericAnswer) answer).getValue();
        if (getValue() == null || answerValue == null) return 1D;
        return DistanceCalculator.calculateNumeric(getValue(), answerValue, question.getMax(), question.getMin());
    }

}
