package com.example.moviemania.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviemania.Others.AccountDetails;
import com.example.moviemania.Others.CircleTransform;
import com.example.moviemania.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {
    ImageView gravatar;
    TextView id,name,username,country,session;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        gravatar=findViewById(R.id.accountGravatar);
        id=findViewById(R.id.accountId);
        name=findViewById(R.id.accountName);
        username=findViewById(R.id.accountUsername);
        country=findViewById(R.id.accountCountry);
        progressBar=findViewById(R.id.accountProgress);
        session=findViewById(R.id.accountSession);
        progressBar.setVisibility(View.VISIBLE);
        getAccount();
    }
    public void getAccount(){
        String url="https://api.themoviedb.org/3/account?api_key="+ AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    id.setText("Account ID : "+parent.getString("id"));
                    name.setText("Name : "+parent.getString("name"));
                    username.setText("Username : "+parent.getString("username"));
                    country.setText("Country : "+parent.getString("iso_3166_1"));
                    Picasso.with(getApplicationContext()).load("https://www.gravatar.com/avatar/"+parent.getJSONObject("avatar").getJSONObject("gravatar").getString("hash")+"?s=128&d=identicon&r=PG").transform(new CircleTransform()).into(gravatar);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        session.setText("Session ID : "+AccountDetails.SESSION_ID);

    }

}
