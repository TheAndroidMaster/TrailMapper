package james.trailmapper.data;

import com.google.android.gms.maps.model.LatLng;

public class PointData {

    private float x, y;
    private LatLng latLng;

    public PointData(float x, float y, double latitude, double longitude) {
        this.x = x;
        this.y = y;
        latLng = new LatLng(latitude, longitude);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
