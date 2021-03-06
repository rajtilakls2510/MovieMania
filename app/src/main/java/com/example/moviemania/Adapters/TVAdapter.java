package com.example.moviemania.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviemania.Fragments.TVAirTodayFragment;
import com.example.moviemania.Fragments.TVOnAirFragment;
import com.example.moviemania.Fragments.TVPopularFragment;
import com.example.moviemania.Fragments.TVTopRatedFragment;

public class TVAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public TVAdapter(FragmentManager fm,int totalTabs) {
        super(fm);
        this.totalTabs=totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TVAirTodayFragment tvAir=new TVAirTodayFragment();
                return tvAir;
            case 1:
                TVOnAirFragment onAir=new TVOnAirFragment();
                return onAir;
            case 2:
                TVPopularFragment popular=new TVPopularFragment();
                return popular;
            case 3:
                TVTopRatedFragment top=new TVTopRatedFragment();
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
