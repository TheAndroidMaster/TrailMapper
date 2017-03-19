package james.trailmapper;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class TrailMapper extends Application implements LocationListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String URL_MAPS = "https://theandroidmaster.github.io/TrailMaps/maps.json";

    private static final String KEY_OFFLINE_MAPS = "offline";

    private static final String KEY_MAP_TRAFFIC = "traffic";
    private static final String KEY_MAP_BUILDINGS = "buildings";
    private static final String KEY_MAP_LOCATION = "location";

    private static final String KEY_UI_COMPASS = "compass";
    private static final String KEY_UI_LOCATION = "locationButton";
    private static final String KEY_UI_ZOOM = "zoomButton";

    private static final String KEY_GESTURE_ROTATE = "rotateGesture";
    private static final String KEY_GESTURE_TILT = "tiltGesture";
    private static final String KEY_GESTURE_ZOOM = "zoomGesture";

    private LocationManager locationManager;
    private SharedPreferences prefs;
    private Gson gson;
    private PositionData position;

    private List<MapData> maps;
    private List<MapData> offlineMaps;

    private List<Listener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        gson = new Gson();
        listeners = new ArrayList<>();
        maps = new ArrayList<>();
        offlineMaps = new ArrayList<>();
        startLocationUpdates();

        new Thread() {
            @Override
            public void run() {
                String response = "";

                try {
                    URL url = new URL(URL_MAPS);
                    URLConnection con = url.openConnection();
                    con.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        response += line;
                    }

                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        MapData mapData = gson.fromJson(array.getJSONObject(i).toString(), MapData.class);
                        for (MapData mapData1 : offlineMaps) {
                            if (mapData.equals(mapData1)) {
                                mapData = mapData1; //this might seem somewhat obsolete but TRUST ME ITS COMPLETELY NECESSARY DON'T REMOVE THIS OR YOU'LL BREAK EVERYTHING
                                break;
                            }
                        }

                        maps.add(mapData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onMapsChanged();
                    }
                });
            }
        }.start();

        String json = prefs.getString(KEY_OFFLINE_MAPS, "");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                offlineMaps.add(gson.fromJson(array.getJSONObject(i).toString(), MapData.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onTerminate();
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

    public List<MapData> getOfflineMaps() {
        return offlineMaps;
    }

    public void addOfflineMap(final MapData map) {
        if (!offlineMaps.contains(map)) {
            DrawableTypeRequest request = map.getDrawable(this);
            if (request != null) {
                request.asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        FileOutputStream out = null;
                        String offlineImage = map.getName().toLowerCase().replaceAll("\\s+", "_") + ".png";

                        try {
                            out = new FileOutputStream(new File(getApplicationInfo().dataDir, offlineImage));
                            resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                        } catch (Exception e) {
                            e.printStackTrace();
                            offlineImage = null;
                        }

                        try {
                            if (out != null)
                                out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        if (offlineImage != null) {
                            map.offlineImage = offlineImage;
                            offlineMaps.add(map);
                            saveOfflineMaps();
                            onMapChanged(map);
                        }
                    }
                });
            }
        }
    }

    public void removeOfflineMap(MapData map) {
        if (offlineMaps.contains(map)) {
            new File(getApplicationInfo().dataDir, map.offlineImage);
            map.offlineImage = null;
            offlineMaps.remove(map);
            saveOfflineMaps();
            onMapChanged(map);
        }
    }

    private void saveOfflineMaps() {
        MapData[] mapDatas = offlineMaps.toArray(new MapData[offlineMaps.size()]);
        prefs.edit().putString(KEY_OFFLINE_MAPS, gson.toJson(mapDatas)).apply();
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
        maps.set(maps.indexOf(map), map);
        for (Listener listener : listeners) {
            listener.onMapChanged(map);
        }
    }

    public void onMapsChanged() {
        for (Listener listener : listeners) {
            listener.onMapsChanged();
        }
    }

    public void applySettings(GoogleMap map) {
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));
        map.setTrafficEnabled(isTrafficEnabled());
        map.setBuildingsEnabled(isBuildingsEnabled());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED)
            map.setMyLocationEnabled(isLocationEnabled());

        UiSettings settings = map.getUiSettings();

        settings.setCompassEnabled(isCompassEnabled());
        settings.setMapToolbarEnabled(false);
        settings.setMyLocationButtonEnabled(isLocationButtonEnabled());
        settings.setZoomControlsEnabled(isZoomControlsEnabled());

        settings.setRotateGesturesEnabled(isRotateGestureEnabled());
        settings.setTiltGesturesEnabled(isTiltGestureEnabled());
        settings.setZoomGesturesEnabled(isZoomGestureEnabled());
    }

    public boolean isTrafficEnabled() {
        return prefs.getBoolean(KEY_MAP_TRAFFIC, false);
    }

    public boolean isBuildingsEnabled() {
        return prefs.getBoolean(KEY_MAP_BUILDINGS, false);
    }

    public boolean isLocationEnabled() {
        return prefs.getBoolean(KEY_MAP_LOCATION, true);
    }

    public boolean isCompassEnabled() {
        return prefs.getBoolean(KEY_UI_COMPASS, true);
    }

    public boolean isLocationButtonEnabled() {
        return prefs.getBoolean(KEY_UI_LOCATION, true);
    }

    public boolean isZoomControlsEnabled() {
        return prefs.getBoolean(KEY_UI_ZOOM, true);
    }

    public boolean isRotateGestureEnabled() {
        return prefs.getBoolean(KEY_GESTURE_ROTATE, true);
    }

    public boolean isTiltGestureEnabled() {
        return prefs.getBoolean(KEY_GESTURE_TILT, false);
    }

    public boolean isZoomGestureEnabled() {
        return prefs.getBoolean(KEY_GESTURE_ZOOM, true);
    }

    public void setTrafficEnabled(boolean isTrafficEnabled) {
        prefs.edit().putBoolean(KEY_MAP_TRAFFIC, isTrafficEnabled).apply();
    }

    public void setBuildingsEnabled(boolean isBuildingsEnabled) {
        prefs.edit().putBoolean(KEY_MAP_BUILDINGS, isBuildingsEnabled).apply();
    }

    public void setLocationEnabled(boolean isLocationEnabled) {
        prefs.edit().putBoolean(KEY_MAP_LOCATION, isLocationEnabled).apply();
    }

    public void setCompassEnabled(boolean isCompassEnabled) {
        prefs.edit().putBoolean(KEY_UI_COMPASS, isCompassEnabled).apply();
    }

    public void setLocationButtonEnabled(boolean isLocationButtonEnabled) {
        prefs.edit().putBoolean(KEY_UI_LOCATION, isLocationButtonEnabled).apply();
    }

    public void setZoomControlsEnabled(boolean isZoomControlsEnabled) {
        prefs.edit().putBoolean(KEY_UI_ZOOM, isZoomControlsEnabled).apply();
    }

    public void setRotateGestureEnabled(boolean isRotateGestureEnabled) {
        prefs.edit().putBoolean(KEY_GESTURE_ROTATE, isRotateGestureEnabled).apply();
    }

    public void setTiltGestureEnabled(boolean isTiltGestureEnabled) {
        prefs.edit().putBoolean(KEY_GESTURE_TILT, isTiltGestureEnabled).apply();
    }

    public void setZoomGestureEnabled(boolean isZoomGestureEnabled) {
        prefs.edit().putBoolean(KEY_GESTURE_ZOOM, isZoomGestureEnabled).apply();
    }

    public static SwitchCompat castToSwitch(View view) {
        return (SwitchCompat) view; //this really shouldn't be necessary but I'm too lazy to read docs
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for (Listener listener : listeners) {
            listener.onPreferenceChanged();
        }
    }

    public interface Listener {
        void onLocationChanged(PositionData position);
        void onProviderEnabled(String s);
        void onProviderDisabled(String s);
        void onMapChanged(MapData map);
        void onMapsChanged();

        void onPreferenceChanged();
    }
}
