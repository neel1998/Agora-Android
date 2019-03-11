package com.neel.agora;

import org.json.JSONException;
import org.json.JSONObject;

public class UserHelper {
    private static UserHelper mUserHelper;
    private String mUsername, mFirstName, mLastName, mPassword, mEmail, mToken;
    static UserHelper getUserHelper(){
        return mUserHelper;
    }
    static UserHelper getUserHelper(JSONObject jsonObject, String password){
        mUserHelper = new UserHelper(jsonObject, password);
        return mUserHelper;
    }
    private UserHelper(JSONObject jsonObject, String password){
        try{
            mUsername = jsonObject.getString("username");
            mFirstName = jsonObject.getString("firstName");
            mLastName = jsonObject.getString("lastName");
            mEmail = jsonObject.getString("email");
            mToken = jsonObject.getJSONObject("token").getString("token");
            mPassword = password;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    static void makeNull(){
        mUserHelper = null;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getToken() {
        return mToken;
    }
}
