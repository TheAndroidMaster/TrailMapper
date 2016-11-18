package james.trailmapper.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.adapters.SimplePagerAdapter;
import james.trailmapper.data.PositionData;

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

        adapter = new SimplePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.explore).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_explore, getTheme()));
        menu.findItem(R.id.map).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_map, getTheme()));
        menu.findItem(R.id.settings).setIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_settings, getTheme()));

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.explore:
                        break;
                    case R.id.map:
                        break;
                    case R.id.settings:
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
}
