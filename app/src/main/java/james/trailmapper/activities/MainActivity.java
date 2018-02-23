package james.trailmapper.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;

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
import james.trailmapper.utils.ImageUtils;
import james.trailmapper.views.SimpleViewPager;

public class MainActivity extends AppCompatActivity implements TrailMapper.Listener {

    @BindView(R.id.viewPager)
    SimpleViewPager viewPager;
    @BindView(R.id.navigationView)
    AHBottomNavigation navigationView;

    private TrailMapper trailMapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        trailMapper = (TrailMapper) getApplicationContext();

        viewPager.setAdapter(new SimplePagerAdapter<>(
                getSupportFragmentManager(),
                new ExploreFragment(),
                new OfflineFragment(),
                new MapFragment(),
                new SettingsFragment()
        ));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navigationView.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        new AHBottomNavigationAdapter(this, R.menu.menu_navigation).setupWithBottomNavigation(navigationView);
        navigationView.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        navigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_new_map).setIcon(ImageUtils.getVectorDrawable(this, R.drawable.ic_add, Color.WHITE));
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
