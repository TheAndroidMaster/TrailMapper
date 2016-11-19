package james.trailmapper.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.adapters.SimplePagerAdapter;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.fragments.ExploreFragment;
import james.trailmapper.fragments.MapFragment;
import james.trailmapper.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements TrailMapper.Listener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    private SimplePagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new SimplePagerAdapter(getSupportFragmentManager(), new ExploreFragment(), new MapFragment(), new SettingsFragment());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    if (i == position) menu.getItem(i).setChecked(true);
                    else menu.getItem(i).setChecked(false);
                }
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
                    case R.id.map:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.settings:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
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
}
