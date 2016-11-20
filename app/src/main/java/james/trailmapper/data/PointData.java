package james.trailmapper.data;

import android.util.Pair;

public class PointData {

    private float x, y;
    private Pair<Double, Double> coordinates;

    public PointData(float x, float y, double latitude, double longitude) {
        this.x = x;
        this.y = y;
        coordinates = new Pair<>(latitude, longitude);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Pair<Double, Double> getCoordinates() {
        return coordinates;
    }
}
