package james.trailmapper.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.utils.ImageUtils;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TrailMapper.Listener {

    public static final String EXTRA_MAP = "james.trailmapper.EXTRA_MAP";

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.map)
    View mapView;

    private TrailMapper trailMapper;
    private GoogleMap googleMap;
    private MapData map;

    private MenuItem download;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        trailMapper = (TrailMapper) getApplicationContext();
        trailMapper.addListener(this);

        map = getIntent().getParcelableExtra(EXTRA_MAP);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(map.getName());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView.setAlpha(0);
    }

    @Override
    protected void onDestroy() {
        trailMapper.removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        trailMapper.applySettings(googleMap);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(map.getLatLng(), 15));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            googleMap.setMyLocationEnabled(true);
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        DrawableTypeRequest request = map.getDrawable(this);
        if (request != null) {
            request.thumbnail(0.1f).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    setDrawable(resource);
                }
            });
        }

        mapView.animate().alpha(1).setDuration(500).setStartDelay(500).start();
    }

    private void setDrawable(Drawable drawable) {
        googleMap.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(ImageUtils.drawableToBitmap(drawable)))
                .position(map.getLatLng(), (int) map.getWidth(), (int) map.getHeight())
                .anchor(map.getAnchorX(), map.getAnchorY())
                .zIndex(100)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        menu.findItem(R.id.action_directions).setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_directions, Color.WHITE));

        download = menu.findItem(R.id.action_download);
        if (map.isOffline()) {
            download.setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_delete, Color.WHITE));
            download.setTitle(R.string.action_delete);
        } else {
            download.setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_download, Color.WHITE));
            download.setTitle(R.string.action_save);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
            case R.id.action_directions:
                LatLng latLng = map.getLatLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude)));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) == null) //might not have google maps, use standard geo uri
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude)));

                startActivity(intent);
                break;
            case R.id.action_download:
                if (!map.isOffline()) {
                    trailMapper.addOfflineMap(map);
                    item.setTitle(R.string.title_downloading);
                } else trailMapper.removeOfflineMap(map);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        trailMapper.startLocationUpdates();
        if (googleMap != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    public void onLocationChanged(PositionData position) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onMapChanged(MapData map) {
        if (map.equals(this.map)) {
            this.map = map;
            if (map.isOffline()) {
                download.setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_delete, Color.WHITE));
                download.setTitle(R.string.action_delete);
            } else {
                download.setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_download, Color.WHITE));
                download.setTitle(R.string.action_save);
            }
        }
    }

    @Override
    public void onMapsChanged() {
    }

    @Override
    public void onPreferenceChanged() {
    }
}
