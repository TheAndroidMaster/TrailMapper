package james.trailmapper.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class SimpleViewPager extends ViewPager {

    private boolean isSwipingEnabled = true;

    public SimpleViewPager(Context context) {
        super(context);
    }

    public SimpleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipingEnabled(boolean isSwipingEnabled) {
        this.isSwipingEnabled = isSwipingEnabled;
    }
}
