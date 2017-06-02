package analysis;

/**
 * Aquesta classe és una extensió de la classe Point i ens aporta informació sobre l'usuari que representa aquell punt i la distància al centroide c del seu cluster
 * Els atributs de la classe son el usuari i la distància al centroide
 */
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
