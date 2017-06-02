package analysis;

import java.util.Set;

/**
 * Aquesta classe modelitza un cluster format per un conjunt de punts i un centroide.
 * Com a atributs tenim el centroide, un conjunt de punts i un booleà que ens indica si hi ha canvis
 */
public class Cluster {

    private Point centroid;
    private Set<UserPoint> points;
    private Boolean hasChanges;

    public Cluster() {
        hasChanges = true;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public Set<UserPoint> getPoints() {
        return points;
    }

    public void setPoints(Set<UserPoint> points) {
        this.points = points;
    }

    public Boolean getHasChanges() {
        return hasChanges;
    }

    public void setHasChanges(Boolean hasChanges) {
        this.hasChanges = hasChanges;
    }
}
