package com.example.moviemania;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OutsideAdapter extends FragmentPagerAdapter {


    private Context mycontext;
    int totalTabs;

    public OutsideAdapter(Context context, FragmentManager fm,int totalTabs){
        super(fm);
        mycontext=context;
        this.totalTabs=totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MovieFragment movie=new MovieFragment();
                return movie;
            case 1:
                TVFragment tv=new TVFragment();
                return tv;
            case 2:
                PeopleFragment people=new PeopleFragment();
                return people;

            case 3:
                FavouriteFragment fav=new FavouriteFragment();
                return fav;
            case 4:
                WatchFragment watch=new WatchFragment();
                return watch;
                case 5:
                    ReviewFragment rew=new ReviewFragment();
                    return  rew;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
