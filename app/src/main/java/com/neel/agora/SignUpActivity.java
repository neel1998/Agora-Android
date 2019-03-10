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

public class SignUpActivity extends AppCompatActivity {

    private String username, password, firstname, lastname, email;
    private ProgressBar signUpProgressBar;
    private AppCompatButton signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpProgressBar = findViewById(R.id.signup_prog);
        signupButton = findViewById(R.id.signup_btn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatEditText usernameEditText = findViewById(R.id.username_signup_edittext);
                username = usernameEditText.getText().toString();
                AppCompatEditText passwordEditText = findViewById(R.id.pswd_signup_edittext);
                password = passwordEditText.getText().toString();
                AppCompatEditText firstnameEditText = findViewById(R.id.firstname_signup_edittext);
                firstname = firstnameEditText.getText().toString();
                AppCompatEditText lastnameEditText = findViewById(R.id.lastname_signup_edittext);
                lastname = lastnameEditText.getText().toString();
                AppCompatEditText emailEditText = findViewById(R.id.email_signup_edittext);
                email = emailEditText.getText().toString();
                signUpProgressBar.setVisibility(View.VISIBLE);
                signupButton.setVisibility(View.INVISIBLE);
                new SignUpTask().execute();
            }
        });

        AppCompatTextView loginText = findViewById(R.id.login_text);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private class SignUpTask extends AsyncTask<Void, Void, Response>{
        @Override
        protected Response doInBackground(Void... voids) {
            Response result = null;
            try{
                OkHttpClient client = new OkHttpClient();
                URL url = new URL("https://agora-rest-api.herokuapp.com/api/v1/auth/signup");
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("identifier", username);
                jsonObject.put("password", password);
                jsonObject.put("email", email);
                jsonObject.put("firstName", firstname);
                jsonObject.put("lastName", lastname);

                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                result = client.newCall(request).execute();
            } catch (JSONException | IOException e ) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Response response) {
            signupButton.setVisibility(View.VISIBLE);
            signUpProgressBar.setVisibility(View.INVISIBLE);
            if (response.body() != null) {
                String result = "";
                try {
                   result = response.body().string();
                   JSONObject jsonObject = new JSONObject(result);
                   Toast.makeText(SignUpActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
