package analysis;

public class UserPoint extends Point {
    private String username;

    public UserPoint(Integer nCoordinates, String username) {
        super(nCoordinates);
        this.username = username;
    }
}
