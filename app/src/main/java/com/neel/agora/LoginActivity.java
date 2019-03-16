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

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton loginButton;
    private ProgressBar loginProgressBar;
    private String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Network.areCredentialsStored(this)){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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

                CredentialStorage credentialStorage = CredentialStorage.getInstance(LoginActivity.this);
                credentialStorage.setCredentials(username, password);
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
    private class LoginTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return Network.login(LoginActivity.this);
        }

        @Override
        protected void onPostExecute(Boolean result){
            loginButton.setVisibility(View.VISIBLE);
            loginProgressBar.setVisibility(View.INVISIBLE);
            if (result){
                Toast.makeText(LoginActivity.this, "Log in Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, "Log in Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
