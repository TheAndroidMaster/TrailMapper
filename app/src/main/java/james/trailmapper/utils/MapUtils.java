package james.trailmapper.utils;

import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

    public static double distance(LatLng start, LatLng end) {
        return 12756274 * Math.asin(Math.sqrt(Math.pow(Math.sin((end.latitude - start.latitude) / 2), 2) + Math.cos(start.latitude) * Math.cos(end.latitude) * Math.pow(Math.sin((end.longitude - start.longitude) / 2), 2)));
    }

    public static double metersToDegrees(double meters) {
        return meters / 111000;
    }

    public static double degreesToMeters(double degrees) {
        return degrees * 111000;
    }

}
