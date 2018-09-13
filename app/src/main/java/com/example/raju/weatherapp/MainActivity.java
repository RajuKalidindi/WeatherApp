package com.example.raju.weatherapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    TextView textView1;
    ImageView imageView;
    Button btn;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather");

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);

        textView1 = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.imageView);

        mQueue= Volley.newRequestQueue(this);
    }

    public void onBackPressed(){
        textView1.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    public void onClicks(View view){
        editText.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        jsonParse();
        progressDialog.dismiss();

    }

    private void jsonParse() {
        String city = editText.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=49ad73993bb1919c77ddabdb1c4800a3";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                JSONArray jsonArray = response.getJSONArray("weather");
                                Log.i("weather",jsonArray.toString());
                                JSONObject weatherData = jsonArray.getJSONObject(0);
                                Log.i("weatherData",weatherData.toString());
                                String weather = weatherData.getString("main");
                                String description = weatherData.getString("description");
                                String id = weatherData.getString("icon");
                                String image = "http://openweathermap.org/img/w/"+id+".png";

                                JSONObject main = response.getJSONObject("main");
                                Log.i("main",main.toString());
                                String temperature = main.getString("temp");
                                temperature = temperature + "°C";
                                String humidity = main.getString("humidity");

                                String visibility = response.getString("visibility");

                                JSONObject wind = response.getJSONObject("wind");
                                Log.i("main",main.toString());
                                String speed = wind.getString("speed");
                                Double sp = Double.parseDouble(speed);
                                sp = sp * 1.60934;
                                speed = Double.toString(sp);


                                JSONObject clouds = response.getJSONObject("clouds");
                                Log.i("main",main.toString());
                                String cloudiness = clouds.getString("all");

                                Picasso.with(getApplicationContext()).load(image).into(imageView);
                                textView1.setText(temperature);
                                textView1.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                textView1.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
            }
        });
        mQueue.add(request);

    }

}
