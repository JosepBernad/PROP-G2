package analysis;

import answer.Answer;

public class Point {

    private Answer[] coordinates;

    public Point(Integer nCoordinates) {
        coordinates = new Answer[nCoordinates];
    }

    public void addCoordinate(Integer variable, Answer value) {
        coordinates[variable] = value;
    }

    public Answer getCoordinate(Integer variable) {
        return coordinates[variable];
    }

    public Integer getNumOfCoordinates() {
        return coordinates.length;
    }
}
