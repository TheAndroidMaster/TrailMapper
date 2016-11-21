package james.trailmapper.data;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class PositionData implements Parcelable {

    private float accuracy;
    private double latitude, longitude;

    public PositionData(Location location) {
        accuracy = location.getAccuracy();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    protected PositionData(Parcel in) {
        accuracy = in.readFloat();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PositionData> CREATOR = new Creator<PositionData>() {
        @Override
        public PositionData createFromParcel(Parcel in) {
            return new PositionData(in);
        }

        @Override
        public PositionData[] newArray(int size) {
            return new PositionData[size];
        }
    };

    public float getAccuracy() {
        return accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(accuracy);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
