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


public class ReviewTVFragment extends Fragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Films> reviewTvList=new ArrayList<>();
    ArrayList<String> genreID=new ArrayList<>();
    ArrayList<String> genreName=new ArrayList<>();
    String imageUrl="https://image.tmdb.org/t/p/w500/";


    public ReviewTVFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_review_tv, container, false);
        progressBar=view.findViewById(R.id.reviewTvProgress);
        recyclerView=view.findViewById(R.id.reviewTvRecycle);
        progressBar.setVisibility(View.VISIBLE);
        getGenres(container);
        getTv(container);
        return view;
    }
    public void getTv(final ViewGroup container){
        String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/rated/tv?api_key="+AccountDetails.API_KEY+"&language=en-US&session_id="+AccountDetails.SESSION_ID+"&sort_by=created_at.asc&page=1";
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
                        film1.setReleaseDate(results.getJSONObject(i).getString("first_air_date"));
                        film1.setRating(results.getJSONObject(i).getString("vote_average")+"/10.0");
                        film1.setInfoType("tv");
                        film1.setPoster(imageUrl+results.getJSONObject(i).getString("poster_path"));
                        String genre="";
                        JSONArray genre_movie=results.getJSONObject(i).getJSONArray("genre_ids");
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
                        reviewTvList.add(film1);
                    }
                    recyclerAdapter=new RecyclerAdapter(reviewTvList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
                    recyclerView.setAdapter(recyclerAdapter);
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
            reviewTvList.clear();
        }
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
