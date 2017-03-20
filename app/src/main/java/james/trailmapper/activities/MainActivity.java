package james.trailmapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.adapters.SimplePagerAdapter;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.fragments.ExploreFragment;
import james.trailmapper.fragments.MapFragment;
import james.trailmapper.fragments.OfflineFragment;
import james.trailmapper.fragments.SettingsFragment;
import james.trailmapper.views.SimpleViewPager;

public class MainActivity extends AppCompatActivity implements TrailMapper.Listener {

    @BindView(R.id.viewPager)
    SimpleViewPager viewPager;
    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    private TrailMapper trailMapper;
    private SimplePagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        trailMapper = (TrailMapper) getApplicationContext();

        adapter = new SimplePagerAdapter(getSupportFragmentManager(), new ExploreFragment(), new OfflineFragment(), new MapFragment(), new SettingsFragment());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((ViewGroup) navigationView.getChildAt(0)).getChildAt(position).callOnClick();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.explore).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_explore, getTheme()));
        menu.findItem(R.id.map).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_map, getTheme()));
        menu.findItem(R.id.settings).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_settings, getTheme()));

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.explore:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.offline:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.map:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.settings:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_map:
                startActivity(new Intent(this, MapMakerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(PositionData position) {
    }

    @Override
    public void onProviderEnabled(String s) {
        //TODO: snackbars ftw
    }

    @Override
    public void onProviderDisabled(String s) {
        //TODO: snackbars ftw
    }

    @Override
    public void onMapChanged(MapData map) {

    }

    @Override
    public void onMapsChanged() {

    }

    @Override
    public void onPreferenceChanged() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        trailMapper.startLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
