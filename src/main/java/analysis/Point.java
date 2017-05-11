package analysis;

import answer.Answer;

import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }
}
