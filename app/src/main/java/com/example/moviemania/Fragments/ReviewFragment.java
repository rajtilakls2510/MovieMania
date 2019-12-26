package com.example.moviemania.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviemania.R;
import com.example.moviemania.Adapters.ReviewAdapter;
import com.google.android.material.tabs.TabLayout;


public class ReviewFragment extends Fragment {
    TabLayout reviewTabs;
    ViewPager reviewPager;

    public ReviewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_review, container, false);
        reviewTabs=view.findViewById(R.id.reviewTabs);
        reviewPager=view.findViewById(R.id.reviewPager);
        reviewTabs.addTab(reviewTabs.newTab().setText("Movie"));
        reviewTabs.addTab(reviewTabs.newTab().setText("Tv Shows"));
        reviewTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        final ReviewAdapter adapter=new ReviewAdapter(getChildFragmentManager(),reviewTabs.getTabCount());
        reviewPager.setAdapter(adapter);
        reviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(reviewTabs));
        reviewTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                reviewPager.setCurrentItem(tab.getPosition());
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
