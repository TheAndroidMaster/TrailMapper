package james.trailmapper.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import james.trailmapper.R;
import james.trailmapper.activities.MapActivity;
import james.trailmapper.data.MapData;

public class MapDataAdapter extends RecyclerView.Adapter<MapDataAdapter.ViewHolder> {

    private Activity activity;
    private List<MapData> maps;

    public MapDataAdapter(Activity activity, List<MapData> maps) {
        this.activity = activity;
        this.maps = maps;
    }

    public void setMaps(List<MapData> maps) {
        this.maps = maps;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MapData map = maps.get(position);

        ((TextView) holder.v.findViewById(R.id.title)).setText(map.getName());

        Drawable drawable = map.getDrawable();
        if (drawable != null)
            ((ImageView) holder.v.findViewById(R.id.image)).setImageDrawable(drawable);
        else {
            DrawableTypeRequest<String> request = map.getDrawable(activity);
            if (request != null) {
                request.into(((ImageView) holder.v.findViewById(R.id.image)));
            }
        }

        holder.v.findViewById(R.id.action_directions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position < 0 || position >= maps.size()) return;

                LatLng latLng = maps.get(position).getLatLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude)));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(activity.getPackageManager()) != null)
                    activity.startActivity(intent);
            }
        });

        holder.v.findViewById(R.id.action_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position < 0 || position >= maps.size()) return;

                Intent intent = new Intent(activity, MapActivity.class);
                intent.putExtra(MapActivity.EXTRA_MAP, maps.get(position));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
                else activity.startActivity(intent);
            }
        });

        holder.v.setAlpha(0);
        holder.v.animate().alpha(1).setDuration(500).start();
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
