package com.example.moviemania;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WatchAdapter extends FragmentPagerAdapter {

    int totalTabs;
    public WatchAdapter(FragmentManager fm,int totalTabs){
        super(fm);
        this.totalTabs=totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                WatchMovieFragment movie=new WatchMovieFragment();
                return movie;
            case 1:
                WatchTVFragment tv=new WatchTVFragment();
                return tv;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
