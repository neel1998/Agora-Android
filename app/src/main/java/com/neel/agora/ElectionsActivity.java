package com.neel.agora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neel.agora.Election.ElectionCardAdapter;
import com.neel.agora.Election.ElectionData;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ElectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        Gson gson = new Gson();
        Type electionType = new TypeToken<ArrayList<ElectionData>>(){}.getType();
        ArrayList<ElectionData> electionData = gson.fromJson(getIntent().getStringExtra("electionData"), electionType);
        ListView electionListView = findViewById(R.id.elections_listview);
        ElectionCardAdapter electionCardAdapter = new ElectionCardAdapter(this, electionData);
        electionListView.setAdapter(electionCardAdapter);
    }
}
