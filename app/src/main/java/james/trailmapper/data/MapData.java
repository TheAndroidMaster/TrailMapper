package james.trailmapper.data;

import android.graphics.drawable.Drawable;
import android.webkit.URLUtil;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapData {

    private String name, image;
    private List<LatLng> points;
    private boolean web;

    public MapData(String name, String image, LatLng... points) {
        this.name = name;
        this.image = image;
        this.points = new ArrayList<>(Arrays.asList(points));
        web = URLUtil.isHttpsUrl(image);
    }

    public String getName() {
        return name;
    }

    public Drawable getDrawable() {
        if (web) {
             return null;
        } else return Drawable.createFromPath(image);
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public LatLng getLatLng() {
        return getAverage(points.toArray(new LatLng[points.size()]));
    }

    public static LatLng getAverage(LatLng... latLngs) {
        double latitude = 0, longitude = 0;
        for (LatLng latLng : latLngs) {
            latitude += latLng.latitude;
            longitude += latLng.longitude;
        }

        return new LatLng(latitude / (latLngs.length + 1), longitude / (latLngs.length + 1));
    }
}
