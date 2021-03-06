package com.example.moviemania.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moviemania.Fragments.PeoplePopularFragment;

public class PeopleAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public PeopleAdapter(FragmentManager fm,int totalTabs){
        super(fm);
        this.totalTabs=totalTabs;

    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                PeoplePopularFragment people=new PeoplePopularFragment();
                return people;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
