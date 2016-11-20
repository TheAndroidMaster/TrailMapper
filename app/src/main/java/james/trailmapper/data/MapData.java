package james.trailmapper.data;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.webkit.URLUtil;

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

    public Pair<Double, Double> getCoordinates() {
        List<Pair<Double, Double>> coordinates = new ArrayList<>();
        for (PointData point : points) {
            coordinates.add(point.getCoordinates());
        }

        return getAverage(coordinates);
    }

    public static Pair<Double, Double> getAverage(List<Pair<Double, Double>> coordinates) {
        double latitude = 0, longitude = 0;
        for (Pair<Double, Double> coordinate : coordinates) {
            latitude += coordinate.first;
            longitude += coordinate.second;
        }

        return new Pair<>(latitude / coordinates.size(), longitude / coordinates.size());
    }
}
