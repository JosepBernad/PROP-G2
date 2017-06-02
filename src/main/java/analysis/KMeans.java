package analysis;


import answer.*;
import survey.Survey;

import java.util.*;

/**
 * Aquesta classe s'encarrega del càlcul del algorisme de k-means.
 * Com a atributs de la classe tenim el id de l'enquesta sobre la qual estem realitzant l'anàlisi i el nombre de coordenades d'aquesta
 */
public class KMeans {
    private Integer surveyId;
    private Integer nCoordinates;

    public KMeans(Integer surveyId) {
        this.surveyId = surveyId;
        this.nCoordinates = Survey.getSurveyById(surveyId).getQuestions().size();
    }

    /**
     * Aquest mètode realitza el càlcul i retorna tants clusters com s'hagn demanat
     * @param k és el nombre de clusters
     * @return una llista de clusters
     */
    public List<Cluster> calc(int k) {

        List<UserPoint> points = createPoints();

        List<Cluster> clusters = selectClusters(k, points);

        while (hasChanges(clusters)) {
            prepareClusters(clusters);
            assignPoints(clusters, points);
            recalcCentroids(clusters);
        }
        return clusters;
    }

    /**
     * Aquest mètode comprova si s'han produït canvis en els clusters
     * @param clusters són els clusters
     * @return un boleà
     */
    private boolean hasChanges(List<Cluster> clusters) {
        Boolean b = false;
        for (Cluster cluster : clusters) b |= cluster.getHasChanges();
        return b;
    }

    /**
     * Aquest mètode assigna cada punt al seu clúster més proper
     * @param clusters són els clusters
     * @param points són els punts
     */
    private void assignPoints(List<Cluster> clusters, List<UserPoint> points) {
        for (UserPoint p : points) {
            Cluster nearest = clusters.get(new Random().nextInt(clusters.size()));
            Double minimumDistance = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                if (cluster.getCentroid() != null) {
                    Double distance = distanceBetweenPoints(p, cluster.getCentroid());
                    if (minimumDistance > distance) {
                        minimumDistance = distance;
                        p.setDistanceToCentroid(distance);
                        nearest = cluster;
                    }
                }
            }
            nearest.getPoints().add(p);
        }
    }

    /**
     * Aquest mètode recalcula el centroide dels clusters
     * @param clusters són els clusters
     */
    private void recalcCentroids(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            Set<UserPoint> points = c.getPoints();
            Point centroid = null;
            if (!points.isEmpty()) {
                centroid = new Point(nCoordinates);
                for (int i = 0; i < nCoordinates; ++i) {
                    List<Answer> answers = new ArrayList<>();
                    for (Point p : points) {
                        answers.add(p.getCoordinate(i));
                    }
                    Answer answer = calculateCentroid(answers);
                    centroid.addCoordinate(i, answer);
                }
            }
            c.setHasChanges(c.getCentroid() != null && !c.getCentroid().equals(centroid));
            c.setCentroid(centroid);
        }

    }

    /**
     * Aquest mètode calcula el centroide d'un conjunt de respostes
     * @param answers són les respostes
     * @return
     */
    private Answer calculateCentroid(List<Answer> answers) {
        Answer a = answers.get(0);
        Answer centroid = null;
        if (a instanceof FreeAnswer) {
            List<FreeAnswer> freeAnswers = new ArrayList<>();
            for (Answer answer : answers) freeAnswers.add((FreeAnswer) answer);
            centroid = FreeAnswer.calculateCentroid(freeAnswers);
        } else if (a instanceof MultivaluedQualitativeAnswer) {
            List<MultivaluedQualitativeAnswer> multivaluedQualitativeAnswers = new ArrayList<>();
            for (Answer answer : answers) multivaluedQualitativeAnswers.add((MultivaluedQualitativeAnswer) answer);
            centroid = MultivaluedQualitativeAnswer.calculateCentroid(multivaluedQualitativeAnswers);
        } else if (a instanceof NumericAnswer) {
            List<NumericAnswer> numericAnswers = new ArrayList<>();
            for (Answer answer : answers) numericAnswers.add((NumericAnswer) answer);
            centroid = NumericAnswer.calculateCentroid(numericAnswers);
        } else if (a instanceof UnivaluedQualitativeAnswer) {
            List<UnivaluedQualitativeAnswer> univaluedQualitativeAnswers = new ArrayList<>();
            for (Answer answer : answers) univaluedQualitativeAnswers.add((UnivaluedQualitativeAnswer) answer);
            centroid = UnivaluedQualitativeAnswer.calculateCentroid(univaluedQualitativeAnswers);
        }
        assert centroid != null;
        centroid.setSurveyId(surveyId);
        centroid.setQuestionId(a.getQuestionId());
        return centroid;
    }

    /**
     * Aquest mètode elimina els punts del cluster
     * @param clusters són els clusters
     */
    private void prepareClusters(List<Cluster> clusters) {
        for (Cluster c : clusters) c.getPoints().clear();
    }

    private List<UserPoint> createPoints() {
        List<UserPoint> points = new ArrayList<>();
        Map<String, Map<Integer, Answer>> answers = Answer.getAnswersBySurveyId(surveyId);

        for (String username : answers.keySet()) {
            UserPoint point = new UserPoint(nCoordinates, username);
            for (Integer questionId : answers.get(username).keySet())
                point.addCoordinate(questionId, answers.get(username).get(questionId));
            points.add(point);
        }
        return points;
    }

    /**
     * Aquest mètode selecciona els clústers inicials de forma aleatoria a partir de la llista de punts donada
     * @param k és el nombre de clusters
     * @param points són els punts
     * @return una llista de clusters
     */
    private List<Cluster> selectClusters(int k, List<UserPoint> points) {
        Collections.shuffle(points);
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < k; ++i) {
            Cluster cluster = new Cluster();
            Point point = points.get(i);
            cluster.setCentroid(point);
            cluster.setPoints(new HashSet<>());
            clusters.add(cluster);
        }
        return clusters;
    }

    /**
     * Aquest mètode calcula la distància existent que hi ha donats dos punts
     * @param p és el primer punt
     * @param t és el segon punt
     * @return un valor
     */
    private Double distanceBetweenPoints(Point p, Point t) {
        Double sum = 0D;
        Integer numOfCoordinates = p.getNumOfCoordinates();
        for (int i = 0; i < numOfCoordinates; ++i) {
            Answer coordinateP = p.getCoordinate(i);
            Answer coordinateT = t.getCoordinate(i);
            sum += coordinateP.calculateDistance(coordinateT);
        }
        return sum / numOfCoordinates;
    }
}
