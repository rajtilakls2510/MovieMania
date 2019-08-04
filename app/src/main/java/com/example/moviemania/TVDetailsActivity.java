package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TVDetailsActivity extends AppCompatActivity {

    CheckBox favourite,tvWatch;
    TextView title,created,rating,firstDate,tvGenre,overview,seasonTitle,airingTitle,episodeTitle,prodCompanyTitle,castTitle,similarTitle,ratingTitle1;
    RecyclerView seasons,airingOn,episodes,companies,cast,similar;
    ImageView  coverPhoto,poster;
    LinearLayout posterLayout,tvRatingLayout;
    RatingBar tvYourRating;
    String id,imageUrl="https://image.tmdb.org/t/p/w500",requestBody;
    int totalPages;
    ArrayList<SmallCard> seasonArray=new ArrayList<>();
    ArrayList<SmallCard> networkArray=new ArrayList<>();
    ArrayList<SmallCard> episodeArray=new ArrayList<>();
    ArrayList<SmallCard> companyArray=new ArrayList<>();
    ArrayList<SmallCard> castArray=new ArrayList<>();
    ArrayList<SmallCard> similarArray=new ArrayList<>();
    RecyclerAdapter1 recyclerAdapter1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvdetails);
        title=findViewById(R.id.tvShowTitle);
        created=findViewById(R.id.tvShowCreated);
        rating=findViewById(R.id.tvShowRating);
        favourite=findViewById(R.id.tvFavourite);
        tvWatch=findViewById(R.id.tvWatch);
        tvRatingLayout=findViewById(R.id.tvRatingLayout);
        ratingTitle1=findViewById(R.id.ratingTitle1);
        tvYourRating=findViewById(R.id.tvYourRating);
        firstDate=findViewById(R.id.tvShowFirstDate);
        tvGenre=findViewById(R.id.tvShowGenres);
        overview=findViewById(R.id.tvShowOverview);
        seasonTitle=findViewById(R.id.seasonsTitle);
        airingTitle=findViewById(R.id.airingTitle);
        episodeTitle=findViewById(R.id.episodeTitle);
        prodCompanyTitle=findViewById(R.id.productCompanyTitle);
        seasons=findViewById(R.id.tvShowSeasons);
        airingOn=findViewById(R.id.tvShowAiringOn);
        episodes=findViewById(R.id.tvShowEpisodes);
        companies=findViewById(R.id.tvShowCompanies);
        coverPhoto=findViewById(R.id.tvShowCoverPhoto);
        poster=findViewById(R.id.tvShowPoster);
        posterLayout=findViewById(R.id.posterLayout1);
        castTitle=findViewById(R.id.castTitle1);
        similarTitle=findViewById(R.id.similarTitle1);
        cast=findViewById(R.id.tvShowCast);
        similar=findViewById(R.id.tvShowSimilar);
        id=getIntent().getStringExtra("ID");
        progressBar=findViewById(R.id.tvDetailsProgress);
        progressBar.setVisibility(View.VISIBLE);

        tvYourRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                postRating();
            }
        });
        favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                postFavourite();
            }
        });
        tvWatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                postWatched();
            }
        });
        getDetails();
        getFavourite();
        getWatched();
        getRating();
        getCast();
        getSimilar();

    }
    public void postWatched(){
        String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/watchlist?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;

        if(tvWatch.isChecked())
            requestBody="{ \"media_type\" : \"tv\", \"media_id\": "+id+", \"watchlist\": true}";
        else
            requestBody="{ \"media_type\" : \"tv\", \"media_id\": "+id+", \"watchlist\": false}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(TVDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                    }
                    else if(parent.getString("status_code").equals("13")){
                        Toast.makeText(TVDetailsActivity.this, "Removed from watchlist", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json;charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return requestBody==null?null:requestBody.getBytes(StandardCharsets.UTF_8);
                }catch (Exception e){
                    return null;}
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void postFavourite(){
        String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/favorite?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;

        if(favourite.isChecked())
            requestBody="{ \"media_type\" : \"tv\", \"media_id\": "+id+", \"favorite\": true}";
        else
            requestBody="{ \"media_type\" : \"tv\", \"media_id\": "+id+", \"favorite\": false}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(TVDetailsActivity.this, "Marked as favourite", Toast.LENGTH_SHORT).show();
                    }
                    else if(parent.getString("status_code").equals("13")){
                        Toast.makeText(TVDetailsActivity.this, "Favourite deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TVDetailsActivity.this, "Favourite posting problem", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json;charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return requestBody==null?null:requestBody.getBytes(StandardCharsets.UTF_8);
                }catch (Exception e){
                    return null;}
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void postRating(){
        String url="https://api.themoviedb.org/3/tv/"+id+"/rating?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;
        requestBody="{ \"value\" : \""+tvYourRating.getRating()*2+"\"}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(TVDetailsActivity.this, "TV Show rated.", Toast.LENGTH_SHORT).show();
                    }
                    if(parent.getString("status_code").equals("12")){
                        Toast.makeText(TVDetailsActivity.this, "TV Show Rated.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TVDetailsActivity.this, "Movie rating problem", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json;charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody==null?null:requestBody.getBytes(StandardCharsets.UTF_8);
                }catch(Exception uee){
                    return null;
                }
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getFavourite(){
        totalPages=1;
        int currentPage=1;
        for(currentPage=1;currentPage<=totalPages;currentPage++) {
            String url = "https://api.themoviedb.org/3/account/" + AccountDetails.ACC_ID + "/favorite/tv?api_key=" + AccountDetails.API_KEY + "&session_id=" + AccountDetails.SESSION_ID + "&language=en-US&sort_by=created_at.asc&page=" + currentPage;
            StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent = new JSONObject(response);
                        totalPages = parent.getInt("total_pages");
                        JSONArray results = parent.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            if (results.getJSONObject(i).getString("id").equals(id)) {
                                favourite.setChecked(true);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TVDetailsActivity.this, "Favourite loading problem", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void getWatched(){
        totalPages=1;
        int currentPage=1;
        for(currentPage=1;currentPage<=totalPages;currentPage++){
            String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/watchlist/tv?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID+"&language=en-US&sort_by=created_at.asc&page="+currentPage;
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent=new JSONObject(response);
                        totalPages=parent.getInt("total_pages");
                        JSONArray results=parent.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            if(results.getJSONObject(i).getString("id").equals(id)){
                                tvWatch.setChecked(true);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TVDetailsActivity.this, "Watch loading problem", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void getRating(){
        totalPages=1;
        int currentPage=1;
        for(currentPage=1;currentPage<=totalPages;currentPage++){
            String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/rated/tv?api_key="+ AccountDetails.API_KEY +"&language=en-US&session_id="+AccountDetails.SESSION_ID+"&sort_by=created_at.asc&page="+currentPage;
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent=new JSONObject(response);
                        totalPages=parent.getInt("total_pages");
                        JSONArray results=parent.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            if(results.getJSONObject(i).getString("id").equals(id)){
                                tvYourRating.setRating(Float.parseFloat(results.getJSONObject(i).getString("rating"))/2);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void getDetails(){
        String url="https://api.themoviedb.org/3/tv/"+id+"?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    Picasso.with(TVDetailsActivity.this).load(imageUrl+parent.getString("backdrop_path")).into(coverPhoto);
                    Picasso.with(TVDetailsActivity.this).load(imageUrl+parent.getString("poster_path")).into(poster);
                    title.setText(parent.getString("name"));
                    String string="";
                    JSONArray createdBy=parent.getJSONArray("created_by");
                    for(int i=0;i<createdBy.length();i++){
                        string=string+createdBy.getJSONObject(i).getString("name")+", ";
                    }
                    try{
                        string=string.substring(0,string.length()-2);
                    }catch(Exception e){}
                    created.setText("Created by: "+string);
                    string="";
                    rating.setText("Rating: "+parent.getInt("vote_average")+"/10.0 ("+parent.getString("vote_count")+" votes)");
                    firstDate.setText("First Air Date: "+parent.getString("first_air_date"));
                    JSONArray genres=parent.getJSONArray("genres");
                    for(int i=0;i<genres.length();i++){
                        string=string+genres.getJSONObject(i).getString("name")+", ";
                    }
                    try{
                        string=string.substring(0,string.length()-2);
                    }catch(Exception e){}
                    tvGenre.setText(string);
                    string="";
                    overview.setText(parent.getString("overview"));
                    JSONArray season=parent.getJSONArray("seasons");
                    for(int i=0;i<season.length();i++){
                        SmallCard film1=new SmallCard();
                        film1.setPoster(imageUrl+season.getJSONObject(i).getString("poster_path"));
                        film1.setTitle("Season "+season.getJSONObject(i).getString("season_number"));
                        film1.setSeason(season.getJSONObject(i).getString("season_number"));
                        film1.setSubtitle(season.getJSONObject(i).getString("air_date").substring(0,4));
                        film1.setID(id);
                        film1.setInfoType("tvSeason");
                        seasonArray.add(film1);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(seasonArray);
                    seasons.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    seasons.setAdapter(recyclerAdapter1);
                    JSONArray network=parent.getJSONArray("networks");
                    for(int i=0;i<network.length();i++){
                        SmallCard film1=new SmallCard();
                        film1.setID(network.getJSONObject(i).getString("id"));
                        film1.setTitle(network.getJSONObject(i).getString("name"));
                        film1.setSubtitle(network.getJSONObject(i).getString("origin_country"));
                        film1.setPoster(imageUrl+network.getJSONObject(i).getString("logo_path"));
                        film1.setInfoType("tvNetwork");
                        networkArray.add(film1);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(networkArray);
                    airingOn.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    airingOn.setAdapter(recyclerAdapter1);
                    JSONObject episodeLast=parent.getJSONObject("last_episode_to_air");
                    SmallCard film1=new SmallCard();
                    film1.setID(id);
                    film1.setTitle(episodeLast.getString("name"));
                    film1.setSubtitle("Season: "+episodeLast.getString("season_number"));
                    film1.setSeason(episodeLast.getString("season_number"));
                    film1.setEpisode(episodeLast.getString("episode_number"));
                    film1.setPoster(imageUrl+episodeLast.getString("still_path"));
                    film1.setInfoType("tvEpisode");
                    episodeArray.add(film1);
                    JSONObject episodeNext=parent.getJSONObject("next_episode_to_air");
                    SmallCard film2=new SmallCard();
                    film2.setID(id);
                    film2.setTitle(episodeNext.getString("name"));
                    film2.setSubtitle("Season: "+episodeNext.getString("season_number"));
                    film1.setSeason(episodeNext.getString("season_number"));
                    film1.setEpisode(episodeNext.getString("episode_number"));
                    film2.setPoster(imageUrl+episodeNext.getString("still_path"));
                    film2.setInfoType("tvEpisode");
                    episodeArray.add(film2);
                    recyclerAdapter1=new RecyclerAdapter1(episodeArray);
                    episodes.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    episodes.setAdapter(recyclerAdapter1);
                    JSONArray prod=parent.getJSONArray("production_companies");
                    for(int i=0;i<prod.length();i++){
                        SmallCard film3=new SmallCard();
                        film3.setID(prod.getJSONObject(i).getString("id"));
                        film3.setTitle(prod.getJSONObject(i).getString("name"));
                        film3.setPoster(imageUrl+prod.getJSONObject(i).getString("logo_path"));
                        film3.setSubtitle(prod.getJSONObject(i).getString("origin_country"));
                        film3.setInfoType("company");
                        companyArray.add(film3);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(companyArray);
                    companies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    companies.setAdapter(recyclerAdapter1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TVDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        seasonArray.clear();
        networkArray.clear();
        episodeArray.clear();
        companyArray.clear();
    }
    public void getCast(){
        String url="https://api.themoviedb.org/3/tv/"+id+"/credits?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    int length=0;
                    JSONArray cast1=parent.getJSONArray("cast");
                    if(cast1.length()>5){
                        length=5;
                    }
                    else
                        length=cast1.length();
                    for(int i=0;i<length;i++){
                        SmallCard film1=new SmallCard();
                        film1.setPoster(imageUrl+cast1.getJSONObject(i).getString("profile_path"));
                        film1.setTitle(cast1.getJSONObject(i).getString("name"));
                        film1.setSubtitle(cast1.getJSONObject(i).getString("character"));
                        film1.setID(cast1.getJSONObject(i).getString("id"));
                        film1.setInfoType("cast");
                        castArray.add(film1);
                    }
                    JSONArray crew=parent.getJSONArray("crew");
                    if(crew.length()>5){
                        length=5;
                    }
                    else
                        length=crew.length();
                    for(int i=0;i<length;i++){
                        SmallCard film1=new SmallCard();
                        film1.setPoster(imageUrl+crew.getJSONObject(i).getString("profile_path"));
                        film1.setTitle(crew.getJSONObject(i).getString("name"));
                        film1.setSubtitle(crew.getJSONObject(i).getString("job"));
                        film1.setID(crew.getJSONObject(i).getString("id"));
                        film1.setInfoType("cast");
                        castArray.add(film1);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(castArray);
                    cast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    cast.setAdapter(recyclerAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TVDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        castArray.clear();
    }
    public void getSimilar(){
        String url="https://api.themoviedb.org/3/tv/"+id+"/similar?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&page=1";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray results=parent.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        SmallCard film1=new SmallCard();
                        film1.setID(results.getJSONObject(i).getString("id"));
                        film1.setPoster(imageUrl+results.getJSONObject(i).getString("poster_path"));
                        film1.setSubtitle(results.getJSONObject(i).getString("vote_average")+"/10.0");
                        film1.setTitle(results.getJSONObject(i).getString("name"));
                        film1.setInfoType("tv");
                        similarArray.add(film1);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(similarArray);
                    similar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    similar.setAdapter(recyclerAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TVDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        similarArray.clear();
    }
}
