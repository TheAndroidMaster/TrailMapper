package james.trailmapper.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import james.trailmapper.utils.MapUtils;

public class MapData implements Parcelable {

    public String name, image, offlineImage;
    public Float anchorX, anchorY;
    public double latitude, longitude, width, height;

    public MapData() {
    }

    public MapData(String name, String image, double latitude, double longitude, double width, double height) {
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.width = width;
        this.height = height;
    }

    protected MapData(Parcel in) {
        name = in.readString();
        image = in.readString();
        if (in.readByte() != 0)
            offlineImage = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        width = in.readDouble();
        height = in.readDouble();
        if (in.readByte() != 0) {
            anchorX = in.readFloat();
            anchorY = in.readFloat();
        }
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

    public void setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public String getName() {
        return name;
    }

    public DrawableTypeRequest getDrawable(Context context) {
        return isOffline() ? Glide.with(context).load(new File(context.getApplicationInfo().dataDir, offlineImage)) : Glide.with(context).load(image);
    }

    public boolean isOffline() {
        return offlineImage != null;
    }

    public float getAnchorX() {
        return anchorX != null ? anchorX : 0.5f;
    }

    public float getAnchorY() {
        return anchorY != null ? anchorY : 0.5f;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude + (MapUtils.metersToDegrees(getWidth()) * (0.5 - getAnchorX())), longitude + (MapUtils.metersToDegrees(getHeight()) * (0.5 - getAnchorY())));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeByte((byte) (isOffline() ? 1 : 0));
        if (isOffline())
            parcel.writeString(offlineImage);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(width);
        parcel.writeDouble(height);
        parcel.writeByte((byte) (anchorX != null && anchorY != null ? 1 : 0));
        if (anchorX != null && anchorY != null) {
            parcel.writeFloat(anchorX);
            parcel.writeFloat(anchorY);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || (obj != null && obj instanceof MapData && ((MapData) obj).getName().equals(getName()));
    }
}
