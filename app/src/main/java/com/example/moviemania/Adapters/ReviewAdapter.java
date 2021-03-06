package com.example.moviemania.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviemania.Fragments.ReviewMovieFragment;
import com.example.moviemania.Fragments.ReviewTVFragment;

public class ReviewAdapter extends FragmentPagerAdapter {

    int totalsTabs;
    public ReviewAdapter(FragmentManager fm,int totalsTabs){
        super(fm);
        this.totalsTabs=totalsTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ReviewMovieFragment movie = new ReviewMovieFragment();
                return movie;
            case 1:
                ReviewTVFragment tv=new ReviewTVFragment();
                return tv;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalsTabs;
    }
}
