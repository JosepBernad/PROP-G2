package analysis;

import java.util.HashSet;
import java.util.Set;

/**
 * Aquesta classe s'encarrega de calcular les distàncies entre els diversos tipos de variables que accepta una enquesta
 */
public class DistanceCalculator {

    /**
     * Retorna la distància (entre 0 i 1) entre dues respostes del tipus NumericAnswer
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 2a resposta
     * @param max és el valor màxim
     * @param min és el valor mínim
     * @return un valor
     */
    public static double calculateNumeric(double a, double b, int max, int min) {
        return Math.abs(a - b) / (max - min);
    }

    /**
     * Retorna la distància (entre 0 i 1) entre dues respostes del tipus UnivaluatedQualitativeAnswer
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 2a resposta
     * @param nValues és el total de valors
     * @return un valor
     */
    public static double calculateSortedUnivaluedQualitative(int a, int b, int nValues) {
        return Math.abs(a - b) / (nValues - 1D);
    }

    /**
     * Retorna la distància (entre 0 i 1) entre dues respostes del tipus UnsortedUnivaluedQualitative
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 2a resposta
     * @return un valor
     */
    public static double calculateUnsortedUnivaluedQualitative(String a, String b) {
        return a.equals(b) ? 0 : 1;
    }

    /**
     * Retorna la distància (entre 0 i 1) entre dues respostes del tipus UnsortedMultivaluedQualitative
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 2a resposta
     * @return un valor
     */
    public static double calculateUnsortedMultivaluedQualitative(Set<String> a, Set<String> b) {
        Set<String> intersection = new HashSet<>();
        intersection.addAll(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>();
        union.addAll(a);
        union.addAll(b);
        return 1 - (double) intersection.size() / union.size();
    }

    /**
     * Retorna la distància (entre 0 i 1) entre dues respostes del tipus FreeAnswer
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 1a resposta
     * @return un valor
     */
    public static double calculateFreeQuestion(String a, String b) {
        return computeLevenshteinDistance(a, b) / Math.max(a.length(), b.length());
    }

    /**
     * Retorna la distància de Levenshtein entre dues paraules
     * @param a és el valor de la 1a resposta
     * @param b és el valor de la 1a resposta
     * @return un valor
     */
    private static double computeLevenshteinDistance(String a, String b) {
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