package com.neel.agora;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CredentialStorage {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";

    private String mUsername;
    private String mPassword;
    private String mToken;

    private SharedPreferences mSharedPreferences;
    private static CredentialStorage credentialStorage;

    public static CredentialStorage getInstance(Context context) {
        if (credentialStorage == null) {
            credentialStorage = new CredentialStorage(context);
        }
        return  credentialStorage;
    }
    private CredentialStorage(Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mUsername = mSharedPreferences.getString(KEY_USERNAME, null);
        mPassword = mSharedPreferences.getString(KEY_PASSWORD, null);
        mToken = mSharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getToken() {
        return mToken;
    }

    public void setCredentials(String username, String password) {
        mUsername = username;
        mPassword = password;
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void setToken(String token) {
        mToken = token;
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }
    public void removeCredentials(){
        mToken = null;
        mPassword = null;
        mUsername = null;
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
