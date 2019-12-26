package com.example.moviemania.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.example.moviemania.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyActivity extends AppCompatActivity {
    TextView compName,country,headquarters,site;
    ImageView logo;
    String ID,url1;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        compName=findViewById(R.id.companyName);
        country=findViewById(R.id.companyCountry);
        headquarters=findViewById(R.id.companyHead);
        site=findViewById(R.id.companySite);
        logo=findViewById(R.id.companyLogo);
        progressBar=findViewById(R.id.companyProgress);
        progressBar.setVisibility(View.VISIBLE);
        ID=getIntent().getStringExtra("ID");
        getDetails();
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                startActivity(i);
            }
        });
    }
    public void getDetails(){
        String url="https://api.themoviedb.org/3/company/"+ID+"?api_key=8265bd1679663a7ea12ac168da84d2e8";
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    compName.setText(parent.getString("name"));
                    country.setText("Origin Country: "+parent.getString("origin_country"));
                    Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+parent.getString("logo_path")).into(logo);
                    headquarters.setText("Headquarters: "+parent.getString("headquarters"));
                    url1=parent.getString("homepage");
                    site.setText("Go to website");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompanyActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.GONE);
    }
}
