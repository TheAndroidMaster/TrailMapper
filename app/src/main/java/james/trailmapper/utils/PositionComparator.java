package james.trailmapper.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class PositionComparator implements Comparator<MapData> {

    private LatLng latLng;

    public PositionComparator(PositionData position) {
        latLng = new LatLng(position.getLatitude(), position.getLongitude());
    }

    @Override
    public int compare(MapData t1, MapData t2) {
        return (int) (MapUtils.distance(latLng, t1.getLatLng()) - MapUtils.distance(latLng, t2.getLatLng()));
    }
}
