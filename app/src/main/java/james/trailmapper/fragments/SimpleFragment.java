package james.trailmapper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import james.trailmapper.TrailMapper;

public abstract class SimpleFragment extends Fragment implements TrailMapper.Listener {

    private TrailMapper trailMapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trailMapper = (TrailMapper) getContext().getApplicationContext();
        trailMapper.addListener(this);
    }

    @Override
    public void onDestroy() {
        trailMapper.removeListener(this);
        super.onDestroy();
    }

    public abstract void onSelect();

    TrailMapper getTrailMapper() {
        return trailMapper;
    }

}
