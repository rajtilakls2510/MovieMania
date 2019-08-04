package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.content.Intent;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    ImageView coverPhoto,moviePoster;
    CheckBox favourite,movieWatch;
    TextView title, tagline,runtime,release,language,rating,genres,summaryTitle,overview,companyTitle,castTitle,similarTitle,ratingTitle;
    RecyclerView companies,cast,similar;
    RecyclerAdapter1 recyclerAdapter;
    RatingBar movieYourRating;
    LinearLayout posterlayout,movieRatinglayout;
    String id,requestBody;
    int totalPages;
    String imageUrl="https://image.tmdb.org/t/p/w500";
    ArrayList<SmallCard> companyArray=new ArrayList<>();
    ArrayList<SmallCard> castArray=new ArrayList<>();
    ArrayList<SmallCard> similarArray=new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        coverPhoto=findViewById(R.id.coverPhoto);
        moviePoster=findViewById(R.id.moviePoster);
        favourite=findViewById(R.id.movieFavourite);
        ratingTitle=findViewById(R.id.ratingTitle);
        movieWatch=findViewById(R.id.movieWatch);
        movieYourRating=findViewById(R.id.movieYourRating);
        movieRatinglayout=findViewById(R.id.movieRatingLayout);
        title=findViewById(R.id.movieTitle);
        tagline=findViewById(R.id.movieTagline);
        runtime=findViewById(R.id.movieRuntime);
        release=findViewById(R.id.movieRelease);
        language=findViewById(R.id.movieLanguage);
        rating=findViewById(R.id.movieRating);
        genres=findViewById(R.id.movieGenres);
        summaryTitle=findViewById(R.id.summaryTitle);
        overview=findViewById(R.id.movieOverview);
        companyTitle=findViewById(R.id.productCompany);
        castTitle=findViewById(R.id.castTitle);
        similarTitle=findViewById(R.id.similarTitle);
        companies=findViewById(R.id.movieCompanies);
        cast=findViewById(R.id.movieCast);
        similar=findViewById(R.id.movieSimilar);
        posterlayout=findViewById(R.id.posterLayout);
        id=getIntent().getStringExtra("ID");
        progressBar=findViewById(R.id.movieDetailsProgress);
        progressBar.setVisibility(View.VISIBLE);

        movieYourRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
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
        movieWatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        getSimilarMovies();

    }
    public void postWatched(){
        String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/watchlist?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;

        if(movieWatch.isChecked())
            requestBody="{ \"media_type\" : \"movie\", \"media_id\": "+id+", \"watchlist\": true}";
        else
            requestBody="{ \"media_type\" : \"movie\", \"media_id\": "+id+", \"watchlist\": false}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(DetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                    }
                    else if(parent.getString("status_code").equals("13")){
                        Toast.makeText(DetailsActivity.this, "Removed from watchlist", Toast.LENGTH_SHORT).show();
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
            requestBody="{ \"media_type\" : \"movie\", \"media_id\": "+id+", \"favorite\": true}";
        else
            requestBody="{ \"media_type\" : \"movie\", \"media_id\": "+id+", \"favorite\": false}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(DetailsActivity.this, "Marked as favourite", Toast.LENGTH_SHORT).show();
                    }
                    else if(parent.getString("status_code").equals("13")){
                        Toast.makeText(DetailsActivity.this, "Favourite deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "Favourite posting problem", Toast.LENGTH_SHORT).show();
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
        String url="https://api.themoviedb.org/3/movie/"+id+"/rating?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;
         requestBody="{ \"value\" : \""+movieYourRating.getRating()*2+"\"}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.getString("status_code").equals("1")){
                        Toast.makeText(DetailsActivity.this, "Movie Rated.", Toast.LENGTH_SHORT).show();
                    }
                    if(parent.getString("status_code").equals("12")){
                        Toast.makeText(DetailsActivity.this, "Movie Rated.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "Movie rating problem", Toast.LENGTH_SHORT).show();
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
        for(currentPage=1;currentPage<=totalPages;currentPage++){
            String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/favorite/movies?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID+"&language=en-US&sort_by=created_at.asc&page="+currentPage;
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent=new JSONObject(response);
                        totalPages=parent.getInt("total_pages");
                        JSONArray results=parent.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            if(results.getJSONObject(i).getString("id").equals(id)){
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
                    Toast.makeText(DetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void getWatched(){
        totalPages=1;
        int currentPage=1;
        for(currentPage=1;currentPage<=totalPages;currentPage++){
            String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/watchlist/movies?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID+"&language=en-US&sort_by=created_at.asc&page="+currentPage;
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent=new JSONObject(response);
                        totalPages=parent.getInt("total_pages");
                        JSONArray results=parent.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            if(results.getJSONObject(i).getString("id").equals(id)){
                                movieWatch.setChecked(true);
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
                    Toast.makeText(DetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
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
            String url="https://api.themoviedb.org/3/account/"+AccountDetails.ACC_ID+"/rated/movies?api_key="+ AccountDetails.API_KEY +"&language=en-US&session_id="+AccountDetails.SESSION_ID+"&sort_by=created_at.asc&page="+currentPage;
            StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parent=new JSONObject(response);
                        totalPages=parent.getInt("total_pages");
                        JSONArray results=parent.getJSONArray("results");
                        for(int i=0;i<results.length();i++){
                            if(results.getJSONObject(i).getString("id").equals(id)){
                                movieYourRating.setRating(Float.parseFloat(results.getJSONObject(i).getString("rating"))/2);
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

        String url="https://api.themoviedb.org/3/movie/"+id+"?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        final StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent = new JSONObject(response);
                    Picasso.with(DetailsActivity.this).load(imageUrl + parent.getString("backdrop_path")).into(coverPhoto);
                    Picasso.with(DetailsActivity.this).load(imageUrl+parent.getString("poster_path")).into(moviePoster);
                    title.setText(parent.getString("title"));
                    tagline.setText(parent.getString("tagline"));
                    runtime.setText("Runtime:- " + parent.getString("runtime") + " minutes");
                    release.setText("Release Date:- " + parent.getString("release_date"));
                    JSONArray lang = parent.getJSONArray("spoken_languages");
                    String string = "";
                    for (int i = 0; i < lang.length(); i++) {
                        string = string + lang.getJSONObject(i).getString("name") + ", ";
                    }
                    string = string.substring(0, string.length() - 2);
                    language.setText(string);
                    string = "";
                    rating.setText("Rating:- " + parent.getString("vote_average") + "/10.0 (" + parent.getString("vote_count") + " votes)");
                    JSONArray getgenres = parent.getJSONArray("genres");
                    for (int i = 0; i < getgenres.length(); i++) {
                        try{ string = string + getgenres.getJSONObject(i).getString("name") + ", ";}
                        catch(Exception e){}
                    }
                    try{
                        string=string.substring(0,string.length()-2);
                    }
                    catch (Exception e){}
                    genres.setText(string);
                    overview.setText(parent.getString("overview"));
                    JSONArray comp = parent.getJSONArray("production_companies");
                    for (int i = 0; i < comp.length(); i++) {
                        SmallCard film = new SmallCard();
                        film.setPoster(imageUrl + comp.getJSONObject(i).getString("logo_path"));
                        film.setTitle(comp.getJSONObject(i).getString("name"));
                        film.setSubtitle(comp.getJSONObject(i).getString("origin_country"));
                        film.setID(comp.getJSONObject(i).getString("id"));
                        film.setInfoType("company");
                        companyArray.add(film);
                    }
                    recyclerAdapter = new RecyclerAdapter1(companyArray);
                    companies.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false));
                    companies.setAdapter(recyclerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        companyArray.clear();
    }
    public void getCast(){
        String url="https://api.themoviedb.org/3/movie/"+id+"/credits?api_key=8265bd1679663a7ea12ac168da84d2e8";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray cast1=parent.getJSONArray("cast");
                    for(int i=0;i<5;i++){
                        SmallCard film=new SmallCard();
                        film.setPoster(imageUrl+cast1.getJSONObject(i).getString("profile_path"));
                        film.setTitle(cast1.getJSONObject(i).getString("name"));
                        film.setSubtitle(cast1.getJSONObject(i).getString("character"));
                        film.setID(cast1.getJSONObject(i).getString("id"));
                        film.setInfoType("cast");
                        castArray.add(film);
                    }
                    JSONArray crew=parent.getJSONArray("crew");
                    for(int i=0;i<5;i++){
                        try{
                        SmallCard film=new SmallCard();
                        film.setPoster(imageUrl+crew.getJSONObject(i).getString("profile_path"));
                        film.setTitle(crew.getJSONObject(i).getString("name"));
                        film.setSubtitle(crew.getJSONObject(i).getString("job"));
                        film.setID(crew.getJSONObject(i).getString("id"));
                        film.setInfoType("cast");
                        castArray.add(film);}
                        catch (Exception e){}
                    }
                    recyclerAdapter=new RecyclerAdapter1(castArray);
                    cast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    cast.setAdapter(recyclerAdapter);
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
        castArray.clear();
    }
    public void getSimilarMovies(){
        String url="https://api.themoviedb.org/3/movie/"+id+"/similar?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&page=1";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray result=parent.getJSONArray("results");
                    for(int i=0;i<result.length();i++){
                        SmallCard film=new SmallCard();
                        film.setPoster(imageUrl+result.getJSONObject(i).getString("poster_path"));
                        film.setTitle(result.getJSONObject(i).getString("title"));
                        film.setSubtitle(result.getJSONObject(i).getString("release_date").substring(0,4));
                        film.setID(result.getJSONObject(i).getString("id"));
                        film.setInfoType("movie");
                        similarArray.add(film);
                    }
                    recyclerAdapter =new RecyclerAdapter1(similarArray);
                    similar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    similar.setAdapter(recyclerAdapter);
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
        similarArray.clear();
    }
}
