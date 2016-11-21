package james.trailmapper.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class MapFragment extends SimpleFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String MAP_BUNDLE_KEY = "mapBundleKey";

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.mapView)
    MapView mapView;

    private GoogleMap map;
    private Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

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
        if (getTrailMapper().getPosition() != null)
            onLocationChanged(getTrailMapper().getPosition());
        else {
            snackbar = Snackbar.make(coordinatorLayout, R.string.msg_getting_location, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
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
        if (snackbar != null) snackbar.dismiss();
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
            for (MapData mapData : getTrailMapper().getMaps()) {
                Pair<Double, Double> coordinates = mapData.getCoordinates();
                map.addMarker(new MarkerOptions().position(new LatLng(coordinates.first, coordinates.second))).setTag(mapData);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() != null && marker.getTag() instanceof MapData) {
            Intent intent = new Intent(getContext(), MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_MAP, (MapData) marker.getTag());
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onSelect() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }
}
