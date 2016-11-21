package james.trailmapper.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import android.webkit.URLUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapData implements Parcelable {

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

    protected MapData(Parcel in) {
        name = in.readString();
        image = in.readString();
        points = in.createTypedArrayList(PointData.CREATOR);
        web = in.readByte() != 0;
    }

    public static final Creator<MapData> CREATOR = new Creator<MapData>() {
        @Override
        public MapData createFromParcel(Parcel in) {
            return new MapData(in);
        }

        @Override
        public MapData[] newArray(int size) {
            return new MapData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeTypedList(points);
        parcel.writeByte((byte) (web ? 1 : 0));
    }
}
