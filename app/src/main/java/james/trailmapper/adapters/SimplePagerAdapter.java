package james.trailmapper.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import james.trailmapper.fragments.SimpleFragment;

public class SimplePagerAdapter extends FragmentStatePagerAdapter {

    private List<SimpleFragment> fragments;

    public SimplePagerAdapter(FragmentManager fm, SimpleFragment... fragments) {
        super(fm);
        this.fragments = new ArrayList<>(Arrays.asList(fragments));
    }

    @Override
    public SimpleFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
