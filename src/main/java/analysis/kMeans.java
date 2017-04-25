package analysis;


import answer.Answer;

import java.util.Iterator;
import java.util.List;


public class kMeans
{
    private  Integer surveyId;

    private double distanceBetweenUsers(String username1, String username2) throws Exception {
        List<Answer> user1Answers = Answer.getAnswersByUsernameAndSurveyID(username1, surveyId);
        List<Answer> user2Answers = Answer.getAnswersByUsernameAndSurveyID(username2, surveyId);

        Iterator<Answer> it1 = user1Answers.iterator();
        Iterator<Answer> it2 = user2Answers.iterator();

        Double sum = 0D;

        while (it1.hasNext() && it2.hasNext())
            sum += it1.next().calculateDistance(it2.next());

        return sum/user1Answers.size();
    }
}
