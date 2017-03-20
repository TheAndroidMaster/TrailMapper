package james.trailmapper.fragments;

import android.support.annotation.Nullable;

import james.trailmapper.data.MapData;

public abstract class MakerFragment extends SimpleFragment {

    private MakerListener listener;
    private boolean isComplete;

    public final void setListener(MakerListener listener) {
        this.listener = listener;
        listener.onCompletionChanged(isComplete);
    }

    @Nullable
    final MapData getMap() {
        if (listener != null)
            return listener.getMap();
        else return null;
    }

    final void changeComletion(boolean isComplete) {
        this.isComplete = isComplete;
        if (listener != null)
            listener.onCompletionChanged(isComplete);
    }

    public final boolean isComplete() {
        return isComplete;
    }

    public interface MakerListener {
        MapData getMap();

        void onCompletionChanged(boolean isComplete);
    }

}
