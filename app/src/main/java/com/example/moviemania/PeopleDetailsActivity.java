package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Collections;

public class PeopleDetailsActivity extends AppCompatActivity {

    TextView peopleName,dept,birth,death,gender,birthPlace,knownTitle,alternativeNames,biographyTitle,biography,movieTitle,tvTitle,other;
    LinearLayout poster;
    ImageView profile;
    RecyclerView movies,tv,othersRecycle;
    RecyclerAdapter1 recyclerAdapter1;
    String id,imageUrl="https://image.tmdb.org/t/p/w500";
    ArrayList<Integer> workYear=new ArrayList<>();
    ArrayList<SmallCard> movieArray=new ArrayList<>();
    ArrayList<SmallCard> tvArray=new ArrayList<>();
    ArrayList<SmallCard> crewArray=new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_details);
        peopleName=findViewById(R.id.peopleName);
        dept=findViewById(R.id.peopleDept);
        birth=findViewById(R.id.peopleBirth);
        death=findViewById(R.id.peopleDeath);
        gender=findViewById(R.id.peopleGender);
        birthPlace=findViewById(R.id.peopleBirthPlace);
        knownTitle=findViewById(R.id.knownTitle);
        alternativeNames=findViewById(R.id.peopleKnown);
        biographyTitle=findViewById(R.id.biographyTitle);
        biography=findViewById(R.id.peopleBiography);
        movieTitle=findViewById(R.id.movieTitle1);
        tvTitle=findViewById(R.id.tvTitle1);
        other=findViewById(R.id.crewTitle);
        poster=findViewById(R.id.posterLayout2);
        profile=findViewById(R.id.profilePhoto);
        movies=findViewById(R.id.recycleMovies);
        tv=findViewById(R.id.recycleTv5);
        othersRecycle=findViewById(R.id.recycleCrew);
        progressBar=findViewById(R.id.peopleDetailsProgress);
        progressBar.setVisibility(View.VISIBLE);
        id=getIntent().getStringExtra("ID");
        getDetails();
        getCredits();
    }
    public void getDetails(){
        String url="https://api.themoviedb.org/3/person/"+id+"?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    peopleName.setText(parent.getString("name"));
                    Picasso.with(getApplicationContext()).load(imageUrl+parent.getString("profile_path")).into(profile);
                    dept.setText("Known for "+parent.getString("known_for_department"));
                    birth.setText("Birthday: "+parent.getString("birthday"));
                    if(parent.getString("deathday")!=null)
                        death.setText("Deathday: "+parent.getString("deathday"));
                    else
                        death.setText("N/A");
                    if(parent.getString("gender").equals("1"))
                        gender.setText("Female");
                    else if(parent.getString("gender").equals("2"))
                        gender.setText("Male");
                    else
                        gender.setText("N/A");
                    birthPlace.setText("Birth Place: "+parent.getString("place_of_birth"));
                    String string="";
                    JSONArray alsoKnown=parent.getJSONArray("also_known_as");
                    for(int i=0;i<alsoKnown.length();i++){
                        try{
                            string=string+alsoKnown.getString(i)+", ";
                        }catch (Exception e){}
                    }
                    try{ string=string.substring(0,string.length()-2);}
                    catch (Exception e){}
                    alternativeNames.setText(string);
                    biography.setText(parent.getString("biography"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PeopleDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
    }
    public void getCredits(){
        String url="https://api.themoviedb.org/3/person/"+id+"/combined_credits?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    JSONArray cast=parent.getJSONArray("cast");
                    for(int i=0;i<cast.length();i++){
                        if(cast.getJSONObject(i).getString("media_type").equals("movie")) {
                            if(cast.getJSONObject(i).has("release_date")){
                            if(cast.getJSONObject(i).getString("release_date").length()>0) {
                                if (!workYear.contains(Integer.parseInt(cast.getJSONObject(i).getString("release_date").substring(0, 4))))
                                    workYear.add(Integer.parseInt(cast.getJSONObject(i).getString("release_date").substring(0, 4)));
                            }
                        }}
                        if(cast.getJSONObject(i).getString("media_type").equals("tv")) {
                            if(cast.getJSONObject(i).has("first_air_date")){
                            if(cast.getJSONObject(i).getString("first_air_date").length()>0) {
                                if (!workYear.contains(Integer.parseInt(cast.getJSONObject(i).getString("first_air_date").substring(0, 4))))
                                    workYear.add(Integer.parseInt(cast.getJSONObject(i).getString("first_air_date").substring(0, 4)));
                            }}}
                        }
                    int size=workYear.size();
                    if(workYear.size()>15)
                        size=15;

                    Collections.sort(workYear, Collections.reverseOrder());
                    workYear=new ArrayList<>(workYear.subList(0,size));
                    for(int i=0;i<cast.length();i++){
                        if(cast.getJSONObject(i).getString("media_type").equals("movie")){
                            if(cast.getJSONObject(i).has("release_date")){
                            if(cast.getJSONObject(i).getString("release_date").length()>0){
                            if(workYear.contains(Integer.parseInt(cast.getJSONObject(i).getString("release_date").substring(0,4)))){
                                SmallCard card1=new SmallCard();
                                card1.setID(cast.getJSONObject(i).getString("id"));
                                card1.setPoster(imageUrl+cast.getJSONObject(i).getString("poster_path"));
                                card1.setTitle(cast.getJSONObject(i).getString("title"));
                                card1.setSubtitle(cast.getJSONObject(i).getString("release_date").substring(0,4));
                                card1.setInfoType("movie");
                                movieArray.add(card1);
                            }}}}

                            if(cast.getJSONObject(i).getString("media_type").equals("tv")){
                                if(cast.getJSONObject(i).has("first_air_date")){
                                if(cast.getJSONObject(i).getString("first_air_date").length()>0){
                                if(workYear.contains(Integer.parseInt(cast.getJSONObject(i).getString("first_air_date").substring(0,4)))){
                                    SmallCard card1=new SmallCard();
                                    card1.setID(cast.getJSONObject(i).getString("id"));
                                    card1.setPoster(imageUrl+cast.getJSONObject(i).getString("poster_path"));
                                    card1.setTitle(cast.getJSONObject(i).getString("name"));
                                    card1.setSubtitle(cast.getJSONObject(i).getString("first_air_date").substring(0,4));
                                    card1.setInfoType("tv");
                                    tvArray.add(card1);
                                }
                            }}}

                    }
                    recyclerAdapter1=new RecyclerAdapter1(movieArray);
                    movies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    movies.setAdapter(recyclerAdapter1);
                    recyclerAdapter1=new RecyclerAdapter1(tvArray);
                    tv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    tv.setAdapter(recyclerAdapter1);
                    JSONArray crew=parent.getJSONArray("crew");
                    for(int i=0;i<crew.length();i++){
                        if(crew.getJSONObject(i).getString("media_type").equals("movie")){

                               SmallCard card=new SmallCard();
                               card.setID(crew.getJSONObject(i).getString("id"));
                               card.setPoster(imageUrl+crew.getJSONObject(i).getString("poster_path"));
                               card.setTitle(crew.getJSONObject(i).getString("title"));
                               if(crew.getJSONObject(i).has("release_date"))
                                   card.setSubtitle(crew.getJSONObject(i).getString("job"));
                               else
                                   card.setSubtitle("");
                               card.setInfoType("movie");
                               crewArray.add(card);
                        }
                        else if(crew.getJSONObject(i).getString("media_type").equals("tv")){

                                SmallCard card=new SmallCard();
                                card.setID(crew.getJSONObject(i).getString("id"));
                                card.setPoster(imageUrl+crew.getJSONObject(i).getString("poster_path"));
                                card.setTitle(crew.getJSONObject(i).getString("name"));
                                if (crew.getJSONObject(i).has("job"))
                                    card.setSubtitle(crew.getJSONObject(i).getString("job"));
                                else
                                    card.setSubtitle("");
                                card.setInfoType("tv");
                                crewArray.add(card);
                        }
                    }
                    recyclerAdapter1=new RecyclerAdapter1(crewArray);
                    othersRecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    othersRecycle.setAdapter(recyclerAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PeopleDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        movieArray.clear();
        tvArray.clear();
        crewArray.clear();
    }
}
