package com.example.moviemania.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemania.Adapters.PeopleAdapter;
import com.example.moviemania.R;
import com.google.android.material.tabs.TabLayout;


public class PeopleFragment extends Fragment {

    TabLayout peopleTabs;
    ViewPager peoplePager;

    public PeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_people, container, false);
        peoplePager=view.findViewById(R.id.peoplePager);
        peopleTabs=view.findViewById(R.id.peopleTabs);
        peopleTabs.addTab(peopleTabs.newTab().setText("Popular"));
        peopleTabs.setTabGravity(peopleTabs.GRAVITY_FILL);
        final PeopleAdapter adapter=new PeopleAdapter(getChildFragmentManager(),peopleTabs.getTabCount());
        peoplePager.setAdapter(adapter);
        peoplePager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(peopleTabs));
        peopleTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                peoplePager.setCurrentItem(tab.getPosition());
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
