package com.example.moviemania;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    TextView tvLog,tvOr;
    Button login,signUp;
    EditText username,password;
    ProgressBar progress;
    String token,requestBody,requestBody1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLog=findViewById(R.id.tvLog);
        tvOr=findViewById(R.id.tvOr);
        login=findViewById(R.id.loginBtn);
        signUp=findViewById(R.id.signUpBtn);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                getToken();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder abuilder=new AlertDialog.Builder(MainActivity.this);
                abuilder.setTitle("Important");
                abuilder.setMessage("There is no way to sign up from this application. You have to go to TheMovieDatabase website to sign up. After completing your email verification and login through the website, you can login here.");
                abuilder.setPositiveButton("Yes, take me.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="https://www.themoviedb.org/account/signup";
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
                abuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog=abuilder.create();
                dialog.show();
            }
        });
    }
    public void getToken(){
        String url="https://api.themoviedb.org/3/authentication/token/new?api_key=8265bd1679663a7ea12ac168da84d2e8";
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.has("success")){
                        token=parent.getString("request_token");
                        requestBody="{"+"\"username\":\""+username.getText().toString()+"\",\"password\":\""+password.getText().toString()+"\",\"request_token\":\""+token+"\"}";
                        getTokenValidation();
                        requestQueue.stop();
                    }
                    else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Failure to receive token. Try again", Toast.LENGTH_SHORT).show();
                        requestQueue.stop();
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Slow Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }
    public void getTokenValidation(){
        String url="https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=8265bd1679663a7ea12ac168da84d2e8";
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.has("success")){
                        requestBody1="{\"request_token\":\""+token+"\"}";
                        getSession();
                        requestQueue.stop();
                    }

                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof AuthFailureError){
                    progress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Incorrect username and password", Toast.LENGTH_LONG).show();
                    requestQueue.stop();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return requestBody==null?null:requestBody.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee){
                    return null;
                }
            }

        };
        requestQueue.add(stringRequest);
    }
    public void getSession(){
        String url="https://api.themoviedb.org/3/authentication/session/new?api_key=8265bd1679663a7ea12ac168da84d2e8";
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    if(parent.has("success")){
                        Intent i=new Intent(MainActivity.this,MovieActivity.class);
                        progress.setVisibility(View.GONE);
                        username.setText("");
                        password.setText("");
                        AccountDetails acc=new AccountDetails();
                        acc.SESSION_ID=parent.getString("session_id");
                        Log.i("Session Id",AccountDetails.SESSION_ID);
                        //Toast.makeText(getApplicationContext(),"You are logged in.",Toast.LENGTH_LONG).show();
                        startActivity(i);
                        getAccountDetails();
                    }
                    else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Login Denied", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Session creation problem", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody1==null?null:requestBody1.getBytes();
                }catch (Exception e){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getAccountDetails(){
        String url="https://api.themoviedb.org/3/account?api_key="+AccountDetails.API_KEY+"&session_id="+AccountDetails.SESSION_ID;
        StringRequest stringRequest=new StringRequest(StringRequest.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parent=new JSONObject(response);
                    AccountDetails acc=new AccountDetails();
                    acc.ACC_ID=parent.getString("id");
                    Toast.makeText(MainActivity.this, "Account id: "+acc.ACC_ID, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Account Loading problem", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        progress.setVisibility(View.GONE);
        startActivity(i);
    }
}
