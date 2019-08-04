package com.example.moviemania;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
