package analysis;


import answer.*;
import answer.Answer.AnswerCollection;

import java.util.*;

public class KMeans {
    public static final int MAX_ITERATIONS = 1;
    private Integer surveyId;
    private AnswerCollection answerCollection;
    private Integer nCoordinates;

    public KMeans(Integer surveyId, AnswerCollection answerCollection, Integer nCoordinates) {
        this.surveyId = surveyId;
        this.answerCollection = answerCollection;
        this.nCoordinates = nCoordinates;
    }

    public List<Cluster> calc(int k) {

        List<Point> points = createPoints();

        List<Cluster> clusters = selectClusters(k, points);
        List<Boolean> hasChanges = new ArrayList<>(k);
        Collections.fill(hasChanges, Boolean.TRUE);

        int i = 0;
        while (i < MAX_ITERATIONS) {
            prepareClusters(clusters);
            assignPoints(clusters, points);
            recalcCentroids(clusters);
            ++i;
        }
        return clusters;
    }

    private void assignPoints(List<Cluster> clusters, List<Point> points) {
        for (Point p : points) {
            Cluster nearest = clusters.get(0);
            Double minimumDistance = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                Double distance = distanceBetweenPoints(p, cluster.getCentroid());
                if (minimumDistance > distance) {
                    minimumDistance = distance;
                    nearest = cluster;
                }
            }
            nearest.getPoints().add(p);
        }
    }


    private void recalcCentroids(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            Point centroid = new Point();
            for (int i = 0; i < nCoordinates; ++i) {
                List<Answer> answers = new ArrayList<>();
                for (Point p : c.getPoints()) {
                    answers.add(p.getCoordinate(i));
                }
                Answer answer = calculateCentroid(answers);
                centroid.addCoordinate(i, answer);
            }
            c.setCentroid(centroid);
        }

    }

    private Answer calculateCentroid(List<Answer> answers) {
        Answer a = answers.get(0);
        if (a instanceof FreeAnswer) {
            List<FreeAnswer> freeAnswers = new ArrayList<>();
            for (Answer answer : answers) freeAnswers.add((FreeAnswer) answer);
            return FreeAnswer.calculateCentroid(freeAnswers);
        } else if (a instanceof MultivaluedQualitativeAnswer) {
            List<MultivaluedQualitativeAnswer> multivaluedQualitativeAnswers = new ArrayList<>();
            for (Answer answer : answers) multivaluedQualitativeAnswers.add((MultivaluedQualitativeAnswer) answer);
            return MultivaluedQualitativeAnswer.calculateCentroid(multivaluedQualitativeAnswers);
        } else if (a instanceof NumericAnswer) {
            List<NumericAnswer> numericAnswers = new ArrayList<>();
            for (Answer answer : answers) numericAnswers.add((NumericAnswer) answer);
            return NumericAnswer.calculateCentroid(numericAnswers);
        } else if (a instanceof UnivaluedQualitativeAnswer) {
            List<UnivaluedQualitativeAnswer> univaluedQualitativeAnswers = new ArrayList<>();
            for (Answer answer : answers) univaluedQualitativeAnswers.add((UnivaluedQualitativeAnswer) answer);
            return UnivaluedQualitativeAnswer.calculateCentroid(univaluedQualitativeAnswers);
        }
        return null;
    }

    private void prepareClusters(List<Cluster> clusters) {
        for (Cluster c : clusters) c.getPoints().clear();
    }

    private List<Point> createPoints() {
        List<Point> points = new ArrayList<>();
        Map<String, Map<Integer, Answer>> answers = answerCollection.getAnswersBySurveyId(surveyId);
        for (String username : answers.keySet()) {
            Point point = new Point();
            for (Integer questionId : answers.get(username).keySet())
                point.addCoordinate(questionId, answers.get(username).get(questionId));
            points.add(point);
        }
        return points;
    }

    private List<Cluster> selectClusters(int k, List<Point> points) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < k; ++i) {
            Cluster cluster = new Cluster();
            Point point = points.get(i);
            cluster.setCentroid(point);
            Set<Point> pointSet = new HashSet<>();
            pointSet.add(point);
            cluster.setPoints(pointSet);
        }
        return clusters;
    }

    private Double distanceBetweenPoints(Point p, Point t) {
        Double sum = 0D;
        Integer numOfCoordinates = p.getNumOfCoordinates();
        for (int i = 0; i < numOfCoordinates; ++i) {
            sum += p.getCoordinate(i).calculateDistance(t.getCoordinate(i));
        }
        return sum / numOfCoordinates;
    }
}
