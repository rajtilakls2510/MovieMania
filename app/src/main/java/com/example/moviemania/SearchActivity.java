package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class SearchActivity extends AppCompatActivity {

    EditText searchText;
    TextView noResult,searchTitle;
    RecyclerView recycleSearch;
    RecyclerAdapter recyclerAdapter;
    ArrayList<String> genreID=new ArrayList<>();
    ArrayList<String> genreName=new ArrayList<>();
    ArrayList<Films> resultList=new ArrayList<>();
    ProgressBar progressBar;
    String imageUrl="https://image.tmdb.org/t/p/w500";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText=findViewById(R.id.search_text);
        noResult=findViewById(R.id.noResult);
        searchTitle=findViewById(R.id.searchTitle);
        recycleSearch=findViewById(R.id.recycleSearch);
        progressBar=findViewById(R.id.searchProgress);
        progressBar.setVisibility(View.GONE);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(),0);
                    progressBar.setVisibility(View.VISIBLE);
                    getSearchResults();
                    Toast.makeText(SearchActivity.this, "Search", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
    public void getSearchResults(){
        String url="https://api.themoviedb.org/3/search/multi?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US&query="+searchText.getText().toString()+"&page=1&include_adult=false";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray results=parent.getJSONArray("results");
                    if(results.length()>0) {
                        recycleSearch.setVisibility(View.VISIBLE);
                        noResult.setVisibility(View.GONE);
                        for (int i = 0; i < results.length(); i++) {
                            if(results.getJSONObject(i).getString("media_type").equals("movie")){
                                Films film1=new Films();
                                film1.setTitle(results.getJSONObject(i).getString("title"));
                                film1.setID(results.getJSONObject(i).getString("id"));
                                film1.setPoster(imageUrl+results.getJSONObject(i).getString("poster_path"));
                                film1.setRating(results.getJSONObject(i).getString("vote_average")+"/10.0");
                                film1.setReleaseDate(results.getJSONObject(i).getString("release_date"));
                                film1.setInfoType("movie");
                                String genre="";
                                JSONArray genre_movie=results.getJSONObject(i).getJSONArray("genre_ids");
                                for(int j=0;j<genre_movie.length();j++){
                                    try{genre=genre+genreName.get(genreID.indexOf(genre_movie.getString(j)))+", ";}
                                    catch (Exception e){}
                                }
                                try{genre=genre.substring(0,genre.length()-2);}
                                catch (Exception e){}
                                film1.setGenres(genre);
                                resultList.add(film1);
                            }
                            if(results.getJSONObject(i).getString("media_type").equals("tv")){
                                Films film1=new Films();
                                film1.setID(results.getJSONObject(i).getString("id"));
                                film1.setTitle(results.getJSONObject(i).getString("name"));
                                film1.setReleaseDate(results.getJSONObject(i).getString("first_air_date"));
                                film1.setRating(results.getJSONObject(i).getString("vote_average")+"/10.0");
                                film1.setPoster(imageUrl+results.getJSONObject(i).getString("poster_path"));

                                film1.setInfoType("tv");
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
                                resultList.add(film1);
                            }
                            if(results.getJSONObject(i).getString("media_type").equals("person")){
                                Films film1=new Films();
                                film1.setID(results.getJSONObject(i).getString("id"));
                                film1.setTitle(results.getJSONObject(i).getString("name"));
                                film1.setPoster(imageUrl+results.getJSONObject(i).getString("profile_path"));
                                String string="";
                                JSONArray known=results.getJSONObject(i).getJSONArray("known_for");
                                for(int j=0;j<known.length();j++){
                                    try{string=string+known.getJSONObject(j).getString("title")+"("+known.getJSONObject(j).getString("release_date").substring(0,4)+"), ";}
                                    catch(Exception e){
                                        try{string=string+known.getJSONObject(j).getString("name")+"("+known.getJSONObject(j).getString("first_air_date").substring(0,4)+"), ";}
                                        catch (Exception e1){}
                                    }
                                }
                                try{string=string.substring(0,string.length()-2);}
                                catch(Exception e){}
                                film1.setRating(string);
                                film1.setInfoType("people");
                                resultList.add(film1);
                            }
                        }
                    }
                    else {
                        recycleSearch.setVisibility(View.GONE);
                        noResult.setVisibility(View.VISIBLE);
                    }
                    recyclerAdapter=new RecyclerAdapter(resultList);
                    recycleSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recycleSearch.setAdapter(recyclerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        resultList.clear();
    }
    public void getGenres() {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Genre loading problem", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }
}
