package james.trailmapper;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class TrailMapper extends Application implements LocationListener {

    private LocationManager locationManager;
    private PositionData position;

    private List<MapData> maps;

    private List<Listener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();
        listeners = new ArrayList<>();
        maps = new ArrayList<>();
        startLocationUpdates();
    }

    public void startLocationUpdates() {
        if (locationManager == null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    @Nullable
    public PositionData getPosition() {
        return position;
    }

    public List<MapData> getMaps() {
        return maps;
    }

    @Override
    public void onLocationChanged(Location location) {
        position = new PositionData(location);
        for (Listener listener : listeners) {
            listener.onLocationChanged(position);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        for (Listener listener : listeners) {
            listener.onProviderEnabled(s);
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        for (Listener listener : listeners) {
            listener.onProviderDisabled(s);
        }
    }

    public void onMapChanged(MapData map) {
        for (Listener listener : listeners) {
            listener.onMapChanged(map);
        }
    }

    public void onMapsChanged() {
        for (Listener listener : listeners) {
            listener.onMapsChanged();
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {
        void onLocationChanged(PositionData position);
        void onProviderEnabled(String s);
        void onProviderDisabled(String s);

        void onMapChanged(MapData map);

        void onMapsChanged();
    }
}
