package james.trailmapper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.adapters.MapDataAdapter;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class OfflineFragment extends SimpleFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MapDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MapDataAdapter(getActivity(), getTrailMapper().getOfflineMaps());
        recyclerView.setAdapter(adapter);

        return v;
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
        onMapsChanged();
    }

    @Override
    public void onMapsChanged() {
        adapter.setMaps(getTrailMapper().getOfflineMaps());
    }
}
