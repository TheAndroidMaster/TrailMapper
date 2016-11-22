package james.trailmapper.utils;

import org.json.JSONException;
import org.json.JSONObject;

import james.trailmapper.data.MapData;

public class JSONUtils {

    public static MapData getMap(JSONObject map) throws JSONException {
        MapData mapData = new MapData(map.getString("name"), map.getString("image"), map.getDouble("latitude"), map.getDouble("longitude"), map.getDouble("width"), map.getDouble("height"));
        if (map.has("anchorX") && map.has("anchorY"))
            mapData.setAnchor((float) map.getDouble("anchorX"), (float) map.getDouble("anchorY"));

        return mapData;
    }

}
