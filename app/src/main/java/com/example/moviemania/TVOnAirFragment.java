package com.example.moviemania;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TVOnAirFragment extends Fragment {

    RecyclerView recycleTv3;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Films> film=new ArrayList<>();
    ArrayList<String> genreID=new ArrayList<>();
    ArrayList<String> genreName=new ArrayList<>();
    ProgressBar progressBar;
    String imageUrl="https://image.tmdb.org/t/p/w500/";
    public TVOnAirFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tvon_air, container, false);
        recycleTv3=view.findViewById(R.id.recycleTv3);
        progressBar=view.findViewById(R.id.tvAirProgress);
        progressBar.setVisibility(View.VISIBLE);
        getGenres(container);
        String url="https://api.themoviedb.org/3/tv/on_the_air?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&page=1";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray result=parent.getJSONArray("results");
                    for(int i=0;i<result.length();i++){
                        Films film1=new Films();
                        film1.setID(result.getJSONObject(i).getString("id"));
                        film1.setTitle(result.getJSONObject(i).getString("name"));
                        film1.setReleaseDate(result.getJSONObject(i).getString("first_air_date"));
                        film1.setRating(result.getJSONObject(i).getString("vote_average")+"/10.0");
                        film1.setInfoType("tv");
                        film1.setPoster(imageUrl+result.getJSONObject(i).getString("poster_path"));
                        String genre="";
                        JSONArray genre_movie=result.getJSONObject(i).getJSONArray("genre_ids");
                        for(int j=0;j<genre_movie.length();j++){
                            try{genre=genre+genreName.get(genreID.indexOf(genre_movie.getString(j)))+", ";}
                            catch(Exception e){
                                //Nothing to do.
                            }
                        }
                        if(genre.length()>1){
                            genre=genre.substring(0,genre.length()-2);}
                        else
                            genre="";
                        film1.setGenres(genre);
                        film.add(film1);
                    }
                    recyclerAdapter=new RecyclerAdapter(film);
                    recycleTv3.setLayoutManager(new LinearLayoutManager(getContext()));
                    recycleTv3.setAdapter(recyclerAdapter);
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
        if(genreID.size()>0){
            RequestQueue requestQueue=Volley.newRequestQueue(container.getContext());
            requestQueue.add(stringRequest);
            progressBar.setVisibility(View.GONE);
            film.clear();
        }
        return view;
    }

    public void getGenres(final ViewGroup container) {
        String url1 = "https://api.themoviedb.org/3/genre/tv/list?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray genres = parent.getJSONArray("genres");
                    for (int i = 0; i < 16; i++) {
                        genreID.add(genres.getJSONObject(i).getString("id"));
                        genreName.add(genres.getJSONObject(i).getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(container.getContext(), "Genre loading problem", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(container.getContext());
        requestQueue1.add(stringRequest);
    }
}
