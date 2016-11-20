package james.trailmapper.data;

import android.graphics.drawable.Drawable;
import android.webkit.URLUtil;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapData {

    private String name, image;
    private List<PointData> points;
    private boolean web;

    public MapData(String name, String image, PointData... points) {
        this.name = name;
        this.image = image;
        this.points = new ArrayList<>(Arrays.asList(points));
        web = URLUtil.isHttpsUrl(image);
    }

    public MapData(String name, String image, List<PointData> points) {
        this.name = name;
        this.image = image;
        this.points = points;
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

    public List<PointData> getPoints() {
        return points;
    }

    public LatLng getLatLng() {
        List<LatLng> latLngs = new ArrayList<>();
        for (PointData point : points) {
            latLngs.add(point.getLatLng());
        }

        return getAverage(latLngs.toArray(new LatLng[points.size()]));
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
