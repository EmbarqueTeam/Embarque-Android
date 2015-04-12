package io.embarque.embarque.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.embarque.embarque.R;
import io.embarque.embarque.fragments.AirportDetailsFragment;
import io.embarque.embarque.fragments.FeedFragment;

public class AirportDetailsAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public AirportDetailsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AirportDetailsFragment();
            default:
                return new FeedFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.details);
            default:
                return context.getString(R.string.feed);
        }
    }
}
