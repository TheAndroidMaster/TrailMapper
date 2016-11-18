package james.trailmapper.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class PositionData {

    private float accuracy;
    private double latitude, longitude;

    public PositionData(Location location) {
        accuracy = location.getAccuracy();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public float getAccuracy() {
        return accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }
}
