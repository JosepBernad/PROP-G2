package analysis;


import answer.Answer;
import answer.Answer.AnswerCollection;
import question.Question;
import survey.Survey;

import java.util.List;

public class kMeans {
    private Integer surveyId;
    private AnswerCollection answerCollection;

    private double distanceBetweenUsers(String username1, String username2) throws Exception {
        List<Question> questions = Survey.getSurveyById(surveyId).getQuestions();

        Double sum = 0D;
        for (Question question : questions) {
            Integer questionId = question.getId();
            Answer answer1 = answerCollection.getAnswer(surveyId, username1, questionId);
            Answer answer2 = answerCollection.getAnswer(surveyId, username2, questionId);
            sum += answer1.calculateDistance(answer2);
        }
        return sum / questions.size();
    }
}
