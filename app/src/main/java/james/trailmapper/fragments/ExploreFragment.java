package james.trailmapper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import james.trailmapper.R;
import james.trailmapper.adapters.MapDataAdapter;
import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;
import james.trailmapper.utils.PositionComparator;

public class ExploreFragment extends SimpleFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PositionData position;
    private List<MapData> maps;
    private MapDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, v);

        maps = getTrailMapper().getMaps();
        if (position != null) Collections.sort(maps, new PositionComparator(position));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MapDataAdapter(getActivity(), maps);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onLocationChanged(PositionData position) {
        this.position = position;
        Collections.sort(maps, new PositionComparator(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapChanged(MapData map) {
        List<MapData> maps = adapter.getMaps();
        if (maps.contains(map))
            adapter.notifyItemChanged(maps.indexOf(map));
    }

    @Override
    public void onMapsChanged() {
        adapter.notifyDataSetChanged();
    }
}
