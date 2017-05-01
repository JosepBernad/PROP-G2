package answer;

import analysis.DistanceCalculator;

public class NumericAnswer extends Answer {

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Double calculateDistance(Answer answer) {
        return DistanceCalculator.calculateDistance(this, (NumericAnswer) answer);
    }


//    public static Answer calculateCentroid(List<Answer> answers) {
//        Float suma = 0F;
//        for (Answer answer : answers) {
//            NumericAnswer numericAnswer = (NumericAnswer) answer;
//            suma += numericAnswer.getValue();
//        }
//
//        NumericAnswer numericAnswer = new NumericAnswer();
//        Float v = suma / answers.size();
//        numericAnswer.setValue(v);
//        return numericAnswer;
//    }

}
