package com.example.moviemania.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.moviemania.Models.Films;
import com.example.moviemania.Others.GenreRetrieveCallback;
import com.example.moviemania.R;
import com.example.moviemania.Adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Films> film;
    ArrayList<String> genreID=new ArrayList<>();
    ArrayList<String> genreName=new ArrayList<>();
    ProgressBar progressBar;

    public UpcomingFragment(){
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        film=new ArrayList<>();
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    public static UpcomingFragment newInstance(){
        return new UpcomingFragment();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle2);
        progressBar=view.findViewById(R.id.movieUpcomingProgress);
        progressBar.setVisibility(View.VISIBLE);
        getGenres(getContext(), new GenreRetrieveCallback() {
            @Override
            public void retrieveGenres() {
                String url="https://api.themoviedb.org/3/movie/upcoming?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&page=1";
                StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject parentJson=new JSONObject(response);
                            JSONArray result=parentJson.getJSONArray("results");
                            for(int i=0;i<20;i++){

                                Films film1=new Films();
                                film1.setTitle(result.getJSONObject(i).getString("title"));
                                film1.setID(result.getJSONObject(i).getString("id"));
                                film1.setPoster("https://image.tmdb.org/t/p/w500/"+result.getJSONObject(i).getString("poster_path"));
                                film1.setRating(result.getJSONObject(i).getString("vote_average")+"/10.0");
                                film1.setReleaseDate(result.getJSONObject(i).getString("release_date"));
                                film1.setInfoType("movie");
                                String genre="";
                                JSONArray genre_movie=result.getJSONObject(i).getJSONArray("genre_ids");
                                for(int j=0;j<genre_movie.length();j++){
                                    try{genre=genre+genreName.get(genreID.indexOf(genre_movie.getString(j)))+", ";}
                                    catch (Exception e){}
                                }
                                if(genre.length()>1)
                                    genre=genre.substring(0,genre.length()-2);
                                film1.setGenres(genre);
                                film.add(film1);
                                recyclerAdapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Slow Internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
                if(genreID.size()>0){
                    RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
                    progressBar.setVisibility(View.GONE);
                    film.clear();
                }
            }
        });
        recyclerAdapter=new RecyclerAdapter(film);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerAdapter);



    }

    public void getGenres(final Context context, final GenreRetrieveCallback genreRetrieveCallback) {
        String url1 = "https://api.themoviedb.org/3/genre/movie/list?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray genres = parent.getJSONArray("genres");
                    for (int i = 0; i < 19; i++) {
                        genreID.add(genres.getJSONObject(i).getString("id"));
                        genreName.add(genres.getJSONObject(i).getString("name"));
                    }
                    genreRetrieveCallback.retrieveGenres();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Genre loading problem", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(context);
        requestQueue1.add(stringRequest);
    }


}
