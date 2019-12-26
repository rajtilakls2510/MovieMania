package com.example.moviemania.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemania.Adapters.FavouriteAdapter;
import com.example.moviemania.R;
import com.google.android.material.tabs.TabLayout;


public class FavouriteFragment extends Fragment {

    TabLayout favTabs;
    ViewPager favPager;

    public FavouriteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_favourite, container, false);
        favTabs=view.findViewById(R.id.favTabs);
        favPager=view.findViewById(R.id.favPager);
        favTabs.addTab(favTabs.newTab().setText("Movies"));
        favTabs.addTab(favTabs.newTab().setText("Tv Shows"));
        favTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        final FavouriteAdapter fav=new FavouriteAdapter(getChildFragmentManager(),favTabs.getTabCount());
        favPager.setAdapter(fav);
        favPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(favTabs));;
        favTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                favPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


}
