package com.example.moviemania;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;


public class TVFragment extends Fragment {

    TabLayout tvTabs;
    ViewPager tvPager;

    public TVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tv, container, false);
        tvPager=view.findViewById(R.id.tvPager);
        tvTabs=view.findViewById(R.id.tvTabs);
        tvTabs.addTab(tvTabs.newTab().setText("Airing Today"));
        tvTabs.addTab(tvTabs.newTab().setText("On Air"));
        tvTabs.addTab(tvTabs.newTab().setText("Popular"));
        tvTabs.addTab(tvTabs.newTab().setText("Top Rated"));
        tvTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tvTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        final TVAdapter adapterTv=new TVAdapter(getChildFragmentManager(),tvTabs.getTabCount());
        tvPager.setAdapter(adapterTv);
        tvPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tvTabs));
        tvTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tvPager.setCurrentItem(tab.getPosition());
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
