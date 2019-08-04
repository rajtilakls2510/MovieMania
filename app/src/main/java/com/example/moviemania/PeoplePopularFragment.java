package com.example.moviemania;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PeoplePopularFragment extends Fragment {

    RecyclerView recyclePeople;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Films> peopleArray=new ArrayList<>();
    ProgressBar progressBar;
    String imageUrl="https://image.tmdb.org/t/p/w500";

    public PeoplePopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_people_popular, container, false);
        recyclePeople=view.findViewById(R.id.recyclePeople);
        progressBar=view.findViewById(R.id.peoplePopularProgress);
        progressBar.setVisibility(View.VISIBLE);
        String url="https://api.themoviedb.org/3/person/popular?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&page=1";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray results=parent.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        Films film1=new Films();
                        film1.setID(results.getJSONObject(i).getString("id"));
                        film1.setTitle(results.getJSONObject(i).getString("name"));
                        film1.setPoster(imageUrl+results.getJSONObject(i).getString("profile_path"));
                        String string="";
                        JSONArray known=results.getJSONObject(i).getJSONArray("known_for");
                        for(int j=0;j<known.length();j++){
                            try{string=string+known.getJSONObject(j).getString("title")+"("+known.getJSONObject(j).getString("release_date").substring(0,4)+"), ";}
                            catch(Exception e){
                                string=string+known.getJSONObject(j).getString("original_name")+"("+known.getJSONObject(j).getString("first_air_date").substring(0,4)+"), ";
                            }
                        }
                        if(string.length()>1){string=string.substring(0,string.length()-2);}

                        film1.setRating(string);
                        film1.setInfoType("people");
                        peopleArray.add(film1);
                    }
                    recyclerAdapter=new RecyclerAdapter(peopleArray);
                    recyclePeople.setLayoutManager(new LinearLayoutManager(container.getContext()));
                    recyclePeople.setAdapter(recyclerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(container.getContext(), "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(container.getContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        peopleArray.clear();
        return view;
    }


}
