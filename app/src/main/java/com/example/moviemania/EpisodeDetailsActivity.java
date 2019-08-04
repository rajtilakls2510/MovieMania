package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class EpisodeDetailsActivity extends AppCompatActivity {

    TextView episodeName,air_date,episodeNumber,seasonNumber,rating,overviewTitle,overview,crewTitle,guestTitle;
    LinearLayout episodeLayout;
    ImageView stillPath;
    RecyclerView crew,guest;
    RecyclerAdapter1 recyclerAdapter1;
    String ID,season,episode,imageUrl="https://image.tmdb.org/t/p/w500";
    ArrayList<SmallCard> crewArray=new ArrayList<>();
    ArrayList<SmallCard> guestArray=new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        episodeName=findViewById(R.id.episodeName);
        air_date=findViewById(R.id.episodeDate);
        episodeNumber=findViewById(R.id.episodeNumber);
        seasonNumber=findViewById(R.id.episodeSeason);
        rating=findViewById(R.id.episodeRating);
        overviewTitle=findViewById(R.id.episodeOverviewTitle);
        overview=findViewById(R.id.episodeOverview);
        crewTitle=findViewById(R.id.crewTitle1);
        guestTitle=findViewById(R.id.guestTitle1);
        episodeLayout=findViewById(R.id.episodeLayout);
        stillPath=findViewById(R.id.stillPath);
        crew=findViewById(R.id.recycleCrew1);
        guest=findViewById(R.id.recycleGuest);
        progressBar=findViewById(R.id.episodeDetailsProgress);
        progressBar.setVisibility(View.VISIBLE);
        ID=getIntent().getStringExtra("TV_ID");
        season=getIntent().getStringExtra("Season");
        episode=getIntent().getStringExtra("Episode");
        getDetails();

    }
    public void getDetails(){
        String url="https://api.themoviedb.org/3/tv/"+ID+"/season/"+season+"/episode/"+episode+"?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    episodeName.setText(parent.getString("name"));
                    Picasso.with(getApplicationContext()).load(imageUrl+parent.getString("still_path")).into(stillPath);
                    air_date.setText("Date: "+parent.getString("air_date"));
                    episodeNumber.setText("Episode: "+parent.getString("episode_number"));
                    seasonNumber.setText("Season: "+parent.getString("season_number"));
                    rating.setText(parent.getString("vote_average")+"/10.0("+parent.getString("vote_count")+" votes)");
                    overview.setText(parent.getString("overview"));
                    JSONArray crew1=parent.getJSONArray("crew");
                    for(int i=0;i<crew1.length();i++){
                        SmallCard card=new SmallCard();
                        card.setID(crew1.getJSONObject(i).getString("id"));
                        card.setPoster(crew1.getJSONObject(i).getString("profile_path"));
                        card.setTitle(crew1.getJSONObject(i).getString("name"));
                        card.setSubtitle(crew1.getJSONObject(i).getString("job"));
                        card.setInfoType("cast");
                        crewArray.add(card);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(crewArray);
                    crew.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    crew.setAdapter(recyclerAdapter1);
                    JSONArray guest1=parent.getJSONArray("guest_stars");
                    for(int i=0;i<guest1.length();i++){
                        SmallCard card=new SmallCard();
                        card.setID(guest1.getJSONObject(i).getString("id"));
                        card.setPoster(imageUrl+guest1.getJSONObject(i).getString("profile_path"));
                        card.setTitle(guest1.getJSONObject(i).getString("name"));
                        card.setSubtitle(guest1.getJSONObject(i).getString("character"));
                        card.setInfoType("cast");
                        guestArray.add(card);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(guestArray);
                    guest.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    guest.setAdapter(recyclerAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EpisodeDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        crewArray.clear();
        guestArray.clear();
    }
}
