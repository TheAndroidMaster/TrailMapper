package james.trailmapper.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import james.trailmapper.fragments.SimpleFragment;

public class SimplePagerAdapter<T extends SimpleFragment> extends FragmentStatePagerAdapter {

    private List<T> fragments;

    @SafeVarargs
    public SimplePagerAdapter(FragmentManager fm, T... fragments) {
        super(fm);
        this.fragments = new ArrayList<>(Arrays.asList(fragments));
    }

    @Override
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
