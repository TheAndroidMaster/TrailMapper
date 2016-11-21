package james.trailmapper.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

public class PointData implements Parcelable {

    private float x, y;
    private Pair<Double, Double> coordinates;

    public PointData(float x, float y, double latitude, double longitude) {
        this.x = x;
        this.y = y;
        coordinates = new Pair<>(latitude, longitude);
    }

    protected PointData(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        coordinates = new Pair<>(in.readDouble(), in.readDouble());
    }

    public static final Creator<PointData> CREATOR = new Creator<PointData>() {
        @Override
        public PointData createFromParcel(Parcel in) {
            return new PointData(in);
        }

        @Override
        public PointData[] newArray(int size) {
            return new PointData[size];
        }
    };

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Pair<Double, Double> getCoordinates() {
        return coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(x);
        parcel.writeFloat(y);
        parcel.writeDouble(coordinates.first);
        parcel.writeDouble(coordinates.second);
    }
}
