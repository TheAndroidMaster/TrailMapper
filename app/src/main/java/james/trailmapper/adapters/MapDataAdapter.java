package james.trailmapper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import james.trailmapper.data.MapData;

public class MapDataAdapter extends RecyclerView.Adapter<MapDataAdapter.ViewHolder> {

    private List<MapData> maps;

    public MapDataAdapter(List<MapData> maps) {
        this.maps = maps;
    }

    public void setMaps(List<MapData> maps) {
        this.maps = maps;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
