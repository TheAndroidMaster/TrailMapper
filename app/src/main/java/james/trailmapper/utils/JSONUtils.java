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

        }

        return new MapData(name, image, points);
    }


}
