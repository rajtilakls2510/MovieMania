package com.example.moviemania;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class WatchFragment extends Fragment {
    TabLayout watchTabs;
    ViewPager watchPager;

    public WatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_watch, container, false);
        watchTabs=view.findViewById(R.id.watchTabs);
        watchPager=view.findViewById(R.id.watchPager);
        watchTabs.addTab(watchTabs.newTab().setText("Movie"));
        watchTabs.addTab(watchTabs.newTab().setText("Tv Shows"));
        watchTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        final WatchAdapter adapter=new WatchAdapter(getChildFragmentManager(),watchTabs.getTabCount());
        watchPager.setAdapter(adapter);
        watchPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(watchTabs));
        watchTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                watchPager.setCurrentItem(tab.getPosition());
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
