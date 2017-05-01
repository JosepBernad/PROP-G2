package analysis;

import answer.Answer;

import java.util.List;

public class Point {

    private List<Answer> coordinates;

    public void addCoordinate(Integer variable, Answer value) {
        coordinates.set(variable, value);
    }

    public Answer getCoordinate(Integer variable) {
        return coordinates.get(variable);
    }
}
