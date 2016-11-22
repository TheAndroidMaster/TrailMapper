package james.trailmapper.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

public class MapData implements Parcelable {

    private String name, image;
    private Float anchorX, anchorY;
    private double latitude, longitude, width, height;
    private boolean web;

    public MapData(String name, String image, double latitude, double longitude, double width, double height) {
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.width = width;
        this.height = height;
        web = URLUtil.isHttpsUrl(image);
    }

    protected MapData(Parcel in) {
        name = in.readString();
        image = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        width = in.readDouble();
        height = in.readDouble();
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

    public void setAnchor(float anchorX, float anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Drawable getDrawable() {
        if (!web) {
            try {
                return Drawable.createFromPath(image);
            } catch (OutOfMemoryError ignored) {
            }
        }

        return null;
    }

    @Nullable
    public DrawableTypeRequest<String> getDrawable(Context context) {
        if (web) return Glide.with(context).load(image);
        else return null;
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
        return new LatLng(latitude + (getWidth() * (0.5 - getAnchorX())), longitude + (getHeight() * (0.5 - getAnchorY())));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(width);
        parcel.writeDouble(height);
        parcel.writeByte((byte) (web ? 1 : 0));
    }
}
