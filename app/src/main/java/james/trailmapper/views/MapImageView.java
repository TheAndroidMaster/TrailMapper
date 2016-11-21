package james.trailmapper.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import james.trailmapper.data.MapData;
import james.trailmapper.data.PositionData;

public class MapImageView extends AppCompatImageView {

    private MapData map;
    private PositionData position;

    public MapImageView(Context context) {
        super(context);
        init();
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

    }

    public void setMapData(MapData map) {
        this.map = map;
        invalidate();
    }

    public void onLocationChanged(PositionData position) {
        this.position = position;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
