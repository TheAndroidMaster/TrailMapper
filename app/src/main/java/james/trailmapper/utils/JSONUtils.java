package james.trailmapper.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import james.trailmapper.data.MapData;
import james.trailmapper.data.PointData;

public class JSONUtils {

    public static MapData getMap(JSONObject map) throws JSONException {
        String name, image;
        List<PointData> points = new ArrayList<>();

        name = map.getString("name");
        image = map.getString("image");

        JSONArray array = map.getJSONArray("points");
        for (int i = 0; i < array.length(); i++) {
            points.add(getPoint(array.getJSONObject(i)));
        }

        return new MapData(name, image, points);
    }

    public static PointData getPoint(JSONObject point) throws JSONException {
        float x, y;
        double latitude, longitude;

        x = (float) point.getDouble("x");
        y = (float) point.getDouble("y");
        latitude = point.getDouble("latitude");
        longitude = point.getDouble("longitude");

        return new PointData(x, y, latitude, longitude);
    }

}
