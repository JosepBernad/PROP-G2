package analysis;

public class UserPoint extends Point {
    private String username;
    private Double distanceToCentroid;

    public UserPoint(Integer nCoordinates, String username) {
        super(nCoordinates);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setDistanceToCentroid(Double distanceToCentroid) {
        this.distanceToCentroid = distanceToCentroid;
    }

    public Double getDistanceToCentroid() {
        return distanceToCentroid;
    }
}
