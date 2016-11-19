package james.trailmapper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class MapFragment extends SimpleFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, TrailMapper.Listener {

    private static final String MAP_BUNDLE_KEY = "mapBundleKey";

    @BindView(R.id.mapView)
    MapView mapView;

    private TrailMapper trailMapper;
    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);
        trailMapper = (TrailMapper) getContext().getApplicationContext();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style));
        map.setOnMarkerClickListener(this);
        if (trailMapper.getPosition() != null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(trailMapper.getPosition().getLatLng(), 13));
        onMapsChanged();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(PositionData position) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position.getLatLng(), 13));
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapChanged(MapData map) {
        onMapsChanged();
    }

    @Override
    public void onMapsChanged() {
        if (map != null) {
            for (MapData mapData : trailMapper.getMaps()) {
                map.addMarker(new MarkerOptions()
                        .position(mapData.getLatLng())
                        .title(mapData.getName())
                        .snippet(getString(R.string.prompt_click))
                ).setTag(mapData);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null && marker.getTag() instanceof MapData) {
            MapData mapData = (MapData) marker.getTag();
            Toast.makeText(getContext(), mapData.getName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
