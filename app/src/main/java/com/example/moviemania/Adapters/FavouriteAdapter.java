package com.example.moviemania.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviemania.Fragments.FavMovieFragment;
import com.example.moviemania.Fragments.FavTVFragment;

public class FavouriteAdapter extends FragmentPagerAdapter {

    int totalTabs;
    public FavouriteAdapter(FragmentManager fm,int totalTabs){
        super(fm);
        this.totalTabs=totalTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FavMovieFragment movie = new FavMovieFragment();
                return movie;
            case 1:
                FavTVFragment tv = new FavTVFragment();
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
