package james.trailmapper.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.TrailMapper;
import james.trailmapper.adapters.SimplePagerAdapter;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.fragments.MakerFragment;
import james.trailmapper.fragments.NameMakerFragment;
import james.trailmapper.views.SimpleViewPager;

public class MapMakerActivity extends AppCompatActivity implements TrailMapper.Listener, MakerFragment.MakerListener {

    public static final String EXTRA_MAP = "james.trailmapper.EXTRA_MAP";

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    SimpleViewPager viewPager;

    private TrailMapper trailMapper;
    private SimplePagerAdapter<MakerFragment> adapter;
    private MapData map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_maker);
        ButterKnife.bind(this);
        trailMapper = (TrailMapper) getApplicationContext();
        trailMapper.addListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NameMakerFragment nameFragment = new NameMakerFragment();
        nameFragment.setListener(this);

        adapter = new SimplePagerAdapter<MakerFragment>(getSupportFragmentManager(), nameFragment);
        viewPager.setAdapter(adapter);

        viewPager.setSwipingEnabled(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setSwipingEnabled(adapter.getItem(position).isComplete());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        map = new MapData();
    }

    @Override
    protected void onDestroy() {
        trailMapper.removeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MapData getMap() {
        return map;
    }

    @Override
    public void onCompletionChanged(boolean isComplete) {
        viewPager.setSwipingEnabled(isComplete);
        if (adapter != null && viewPager.getCurrentItem() == adapter.getCount() - 1)
            trailMapper.addOfflineMap(map);
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
        if (this.map.equals(map))
            finish();
    }

    @Override
    public void onMapsChanged() {
    }

    @Override
    public void onPreferenceChanged() {
    }
}
