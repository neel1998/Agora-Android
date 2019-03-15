package com.neel.agora;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar homeProgressBar;
    TextView totalElectionValue, pendingElectionValue, activeElectionValue, finishedElectionValue;
    UserHelper mUserHelper;
    JSONArray electionDataArray = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeProgressBar = findViewById(R.id.home_prog);
        totalElectionValue = findViewById(R.id.total_election_value);
        activeElectionValue = findViewById(R.id.active_election_value);
        pendingElectionValue = findViewById(R.id.pending_election_value);
        finishedElectionValue = findViewById(R.id.finished_election_value);

        mUserHelper = UserHelper.getUserHelper();

        new ElectionDataTask().execute();
    }

    private class ElectionDataTask extends AsyncTask<Void, Void, NetworkResponse>{
        @Override
        protected NetworkResponse doInBackground(Void... voids) {
            return Network.request("https://agora-rest-api.herokuapp.com/api/v1/election", null, HomeActivity.this, false);
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            homeProgressBar.setVisibility(View.GONE);
            if (response != null){
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseString());
                    if (jsonObject.has("elections")) {
                        electionDataArray = jsonObject.getJSONArray("elections");
                        totalElectionValue.setText(String.valueOf(electionDataArray.length()));
                    }else {
                        Toast.makeText(HomeActivity.this, "Error fetching election data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(HomeActivity.this, "Error fetching election data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                UserHelper.makeNull();
                CredentialStorage.getInstance(this).removeCredentials();
                Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_elections:
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_about:
                Log.d("navbar", "about selected");
                Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
