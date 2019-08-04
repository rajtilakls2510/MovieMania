package com.example.moviemania;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MovieAdapter extends FragmentPagerAdapter {
    int totalTabs;
    public MovieAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs=totalTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                UpcomingFragment upcoming=new UpcomingFragment();
                return upcoming;
            case 1:
                NowPlayingFragment nowPlay=new NowPlayingFragment();
                return nowPlay;

                case 2:
                    PopularFragment popular=new PopularFragment();
                    return popular;
                case 3:
                    TopRatedFragment top=new TopRatedFragment();
                    return top;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }


}
