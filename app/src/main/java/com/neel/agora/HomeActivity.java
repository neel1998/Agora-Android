package com.neel.agora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("token"));
            token = jsonObject.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("token",token);
    }
}
