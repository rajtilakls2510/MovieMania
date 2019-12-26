package com.example.moviemania.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemania.Adapters.MovieAdapter;
import com.example.moviemania.R;
import com.google.android.material.tabs.TabLayout;


public class MovieFragment extends Fragment {

    TabLayout movieTabs;
    ViewPager moviePager;
    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_movie, container, false);
        movieTabs=(TabLayout)view.findViewById(R.id.movieTabs);
        moviePager=(ViewPager) view.findViewById(R.id.moviePager);
        movieTabs.addTab(movieTabs.newTab().setText("Upcoming"));
        movieTabs.addTab(movieTabs.newTab().setText("Now Playing"));
        movieTabs.addTab(movieTabs.newTab().setText("Popular"));
        movieTabs.addTab(movieTabs.newTab().setText("Top Rated"));
        movieTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        movieTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        final MovieAdapter adapterMovie=new MovieAdapter(getChildFragmentManager(),movieTabs.getTabCount());
        moviePager.setAdapter(adapterMovie);
        moviePager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(movieTabs));
        movieTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                moviePager.setCurrentItem(tab.getPosition());
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
