package com.example.moviemania.Fragments;

import android.content.Context;
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
import com.example.moviemania.Others.AccountDetails;
import com.example.moviemania.Models.Films;
import com.example.moviemania.Others.GenreRetrieveCallback;
import com.example.moviemania.R;
import com.example.moviemania.Adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FavMovieFragment extends Fragment {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Films> favMoviesArray=new ArrayList<>();
    ArrayList<String> genreID=new ArrayList<>();
    ArrayList<String> genreName=new ArrayList<>();
    public FavMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fav_movie, container, false);
        recyclerView=view.findViewById(R.id.favMovieRecycle);
        progressBar=view.findViewById(R.id.favMovieProgress);
        progressBar.setVisibility(View.VISIBLE);
        getGenres(container.getContext(), new GenreRetrieveCallback() {
            @Override
            public void retrieveGenres() {
                getMovies(container);
            }
        });

        return view;
    }
    public void getMovies(final ViewGroup container){
        String url="https://api.themoviedb.org/3/account/"+ AccountDetails.ACC_ID+"/favorite/movies?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID+"&language=en-US&sort_by=created_at.asc&page=1";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray results=parent.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        Films film1=new Films();
                        film1.setTitle(results.getJSONObject(i).getString("title"));
                        film1.setPoster("https://image.tmdb.org/t/p/w500"+results.getJSONObject(i).getString("poster_path"));
                        film1.setRating(results.getJSONObject(i).getString("vote_average")+"/10.0");
                        film1.setID(results.getJSONObject(i).getString("id"));
                        film1.setReleaseDate(results.getJSONObject(i).getString("release_date"));
                        film1.setInfoType("movie");
                        String genres="";
                        JSONArray genre_movie=results.getJSONObject(i).getJSONArray("genre_ids");
                        for(int j=0;j<genre_movie.length();j++){
                            try{genres=genres+genreName.get(genreID.indexOf(genre_movie.getString(j)))+", ";}
                            catch (Exception e){}
                        }
                        if(genres.length()>1)
                            genres=genres.substring(0,genres.length()-2);
                        film1.setGenres(genres);
                        favMoviesArray.add(film1);
                    }
                    recyclerAdapter=new RecyclerAdapter(favMoviesArray);
                    recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
                    recyclerView.setAdapter(recyclerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        if(genreID.size()>0){
            RequestQueue requestQueue=Volley.newRequestQueue(container.getContext());
            requestQueue.add(stringRequest);
            progressBar.setVisibility(View.GONE);
            favMoviesArray.clear();
        }

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
