package james.trailmapper.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import james.trailmapper.BR;
import james.trailmapper.R;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class SettingsFragment extends SimpleFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setVariable(BR.trailMapper, getContext().getApplicationContext());

        return binding.getRoot();
    }

    @Override
    public void onSelect() {

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

    }

    @Override
    public void onMapsChanged() {

    }

    @Override
    public void onPreferenceChanged() {

    }
}
