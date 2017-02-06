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

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

public class TrailMapper extends Application implements LocationListener {

    private static String MAPS_URL = "https://theandroidmaster.github.io/TrailMaps/maps.json";

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
                    URL url = new URL(MAPS_URL);
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

        String json = prefs.getString("offline", "");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                offlineMaps.add(gson.fromJson(array.getJSONObject(i).toString(), MapData.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        prefs.edit().putString("offline", gson.toJson(mapDatas)).apply();
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
