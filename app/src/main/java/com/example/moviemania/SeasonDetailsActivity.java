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

public class SeasonDetailsActivity extends AppCompatActivity {

    TextView seasonName,air_date,seasonNumber,overviewTitle,overview,episodeTitle,episodeCount;
    ImageView seasonPoster;
    LinearLayout seasonPosterLayout;
    RecyclerView recyclerView;
    RecyclerAdapter1 recyclerAdapter1;
    ProgressBar progressBar;
    String tvid,season,imageUrl="https://image.tmdb.org/t/p/w500";
    ArrayList<SmallCard> episodeArray=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_details);
        tvid=getIntent().getStringExtra("TV_ID");
        season=getIntent().getStringExtra("Season");
        seasonName=findViewById(R.id.seasonName);
        air_date=findViewById(R.id.seasonAirDate);
        seasonNumber=findViewById(R.id.seasonNumber);
        overviewTitle=findViewById(R.id.overviewTitle);
        overview=findViewById(R.id.seasonOverview);
        episodeTitle=findViewById(R.id.episodeTitle1);
        seasonPoster=findViewById(R.id.seasonPoster);
        seasonPosterLayout=findViewById(R.id.seasonPosterLayout);
        recyclerView=findViewById(R.id.recycleEpisodes);
        episodeCount=findViewById(R.id.seasonEpisodeCount);
        progressBar=findViewById(R.id.seasonDetailsProgress);
        progressBar.setVisibility(View.VISIBLE);
        getDetails();
    }
    public void getDetails(){
        String url="https://api.themoviedb.org/3/tv/"+tvid+"/season/"+season+"?api_key=8265bd1679663a7ea12ac168da84d2e8&language=en-US";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    seasonName.setText(parent.getString("name"));
                    Picasso.with(getApplicationContext()).load(imageUrl+parent.getString("poster_path")).into(seasonPoster);
                    air_date.setText("Air Date: "+parent.getString("air_date"));
                    seasonNumber.setText("Season: "+parent.getString("season_number"));
                    overview.setText(parent.getString("overview"));
                    JSONArray episodes=parent.getJSONArray("episodes");
                    episodeCount.setText("Episodes: "+episodes.length());
                    for(int i=0;i<episodes.length();i++){
                        SmallCard card=new SmallCard();
                        card.setID(tvid);
                        card.setTitle(episodes.getJSONObject(i).getString("name"));
                        card.setSubtitle("Episode "+episodes.getJSONObject(i).getString("episode_number"));
                        card.setPoster(imageUrl+episodes.getJSONObject(i).getString("still_path"));
                        card.setInfoType("tvEpisode");
                        card.setSeason(episodes.getJSONObject(i).getString("season_number"));
                        card.setEpisode(episodes.getJSONObject(i).getString("episode_number"));
                        episodeArray.add(card);
                    }
                    recyclerAdapter1=new RecyclerAdapter1(episodeArray);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                    recyclerView.setAdapter(recyclerAdapter1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SeasonDetailsActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
        episodeArray.clear();
    }
}
