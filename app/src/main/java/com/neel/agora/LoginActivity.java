package com.neel.agora;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton loginButton;
    private ProgressBar loginProgressBar;
    private String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginProgressBar = findViewById(R.id.login_prog);
        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatEditText usernameEditText = findViewById(R.id.username_login_edittext);
                username = usernameEditText.getText().toString();

                AppCompatEditText passwordEditText = findViewById(R.id.pswd_login_edittext);
                password = passwordEditText.getText().toString();

                loginButton.setVisibility(View.INVISIBLE);
                loginProgressBar.setVisibility(View.VISIBLE);

                new LoginTask().execute();
            }
        });

        AppCompatTextView signUpText = findViewById(R.id.signup_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private class LoginTask extends AsyncTask<Void, Void, Response>{
        @Override
        protected Response doInBackground(Void... voids) {
            Response result = null;
            try{
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                URL url = new URL("https://agora-rest-api.herokuapp.com/api/v1/auth/login");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("identifier", username);
                jsonObject.put("password", password);

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                                  .url(url)
                                  .post(body)
                                  .build();
                result = client.newCall(request).execute();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Response response) {
            loginButton.setVisibility(View.VISIBLE);
            loginProgressBar.setVisibility(View.INVISIBLE);
            if (response.body() != null){
                String result = "";
                try {
                    result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    Toast.makeText(LoginActivity.this, "Log in Succesfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("token", jsonObject.getJSONObject("token").toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    if (result.length() > 0) {
                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Log in Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "Log in Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
