package analysis;

import answer.Answer.AnswerCollection;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class kMeansTest {

    @Test
    @Ignore("under develop")
    public void test() {
        // Arrange
        Integer surveyId = 1;
        Integer nCoordinates = 2;
        AnswerCollection answerCollection = new AnswerCollection();
        kMeans kMeans = new kMeans(surveyId, answerCollection, nCoordinates);

        // Act
        List<Cluster> calc = kMeans.calc(3);

        // Assert
        assertTrue(calc.size() == 3);
    }
}
