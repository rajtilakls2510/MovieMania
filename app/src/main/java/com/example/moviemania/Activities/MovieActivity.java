package com.example.moviemania.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviemania.Others.AccountDetails;
import com.example.moviemania.Others.CustomHurlStack;
import com.example.moviemania.Adapters.OutsideAdapter;
import com.example.moviemania.R;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class MovieActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Movie"));
        tabLayout.addTab(tabLayout.newTab().setText("TV"));
        tabLayout.addTab(tabLayout.newTab().setText("People"));
        tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
        tabLayout.addTab(tabLayout.newTab().setText("Watchlist"));
        tabLayout.addTab(tabLayout.newTab().setText("Rated"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final OutsideAdapter adapter1=new OutsideAdapter(MovieActivity.this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSearch:
                Intent i=new Intent(MovieActivity.this, SearchActivity.class);
                startActivity(i);
                return true;
            case R.id.menuLogout:
                deleteSession();
                Intent i2=new Intent(MovieActivity.this,MainActivity.class);
                startActivity(i2);
                finish();
                return true;
            case R.id.menuAccount:
                Intent i3=new Intent(MovieActivity.this,AccountActivity.class);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
    public void deleteSession(){
        String url="https://api.themoviedb.org/3/authentication/session?api_key="+ AccountDetails.API_KEY;
        final String requestBody="{ \"session_id\" : \""+ AccountDetails.SESSION_ID+"\"}";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.has("success")){
                        if (parent.getBoolean("success"))
                        Toast.makeText(MovieActivity.this, "You are logged out.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MovieActivity.this, "Logout unsuccessful", Toast.LENGTH_SHORT).show();
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
                }
                catch (Exception e){
                    return null;
                }
            }
        };
        CustomHurlStack stack=new CustomHurlStack();
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext(),stack);
        requestQueue.add(stringRequest);
    }
}
