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
}
