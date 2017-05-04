package analysis;

import answer.FreeAnswer;
import answer.MultivaluedQualitativeAnswer;
import answer.NumericAnswer;
import answer.UnivaluedQualitativeAnswer;
import question.NumericQuestion;
import question.QualitativeQuestion;

import java.util.HashSet;
import java.util.Set;

public class DistanceCalculator {

    public static Double calculateDistance(NumericAnswer answer1, NumericAnswer answer2, NumericQuestion question) {
        Double value1 = answer1.getValue();
        Double value2 = answer2.getValue();
        return calculateNumeric(value1, value2, question.getMax(), question.getMin());
    }

    public static Double calculateDistance(MultivaluedQualitativeAnswer answer1, MultivaluedQualitativeAnswer answer2) {
        Set<String> values1 = answer1.getValues();
        Set<String> values2 = answer2.getValues();
        return calculateUnsortedMultivaluedQualitative(values1, values2);
    }

    public static Double calculateDistanceUnsorted(UnivaluedQualitativeAnswer answer1, UnivaluedQualitativeAnswer answer2) {
        String value1 = answer1.getOption().getValue();
        String value2 = answer2.getOption().getValue();
        return calculateUnsortedUnivaluedQualitative(value1, value2);
    }

    public static Double calculateDistanceSorted(UnivaluedQualitativeAnswer answer1, UnivaluedQualitativeAnswer answer2, QualitativeQuestion question) {
        Integer weight1 = answer1.getOption().getWeight();
        Integer weight2 = answer2.getOption().getWeight();
        Integer nValues = question.getOptions().size();
        return calculateSortedUnivaluedQualitative(weight1, weight2, nValues);
    }

    public static Double calculateDistance(FreeAnswer answer1, FreeAnswer answer2) {
        String value1 = answer1.getValue();
        String value2 = answer2.getValue();
        return calculateFreeQuestion(value1, value2);
    }

    private static double calculateNumeric(double a, double b, int max, int min) {
        return Math.abs(a - b) / (max - min);
    }

    private static double calculateSortedUnivaluedQualitative(int a, int b, int nValues) {
        return Math.abs(a - b) / (nValues - 1);
    }

    private static double calculateUnsortedUnivaluedQualitative(String a, String b) {
        if (a.equals(b))
            return 1;
        else
            return 0;
    }

    private static double calculateUnsortedMultivaluedQualitative(Set<String> a, Set<String> b) {
        Set<String> intersection = new HashSet<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>();
        union.addAll(a);
        union.addAll(b);
        return 1 - intersection.size() / union.size();
    }

    private static double calculateFreeQuestion(String a, String b) {
        return computeLevenshteinDistance(a, b) / Math.max(a.length(), b.length());
    }

    private static int computeLevenshteinDistance(String a, String b) {
        char[] charA = a.toCharArray();
        char[] charB = b.toCharArray();
        int[][] distance = new int[charA.length + 1][charB.length + 1];

        for (int i = 0; i <= charA.length; i++)
            distance[i][0] = i;
        for (int j = 0; j <= charB.length; j++)
            distance[0][j] = j;
        for (int i = 1; i <= charA.length; i++)
            for (int j = 1; j <= charB.length; j++) {
                int d1 = distance[i - 1][j] + 1;
                int d2 = distance[i][j - 1] + 1;
                int d3 = distance[i - 1][j - 1] +
                        ((charA[i - 1] == charB[j - 1]) ? 0 : 1);
                distance[i][j] = Math.min(Math.min(d1, d2), d3);
            }

        return distance[charA.length][charB.length];
    }

}