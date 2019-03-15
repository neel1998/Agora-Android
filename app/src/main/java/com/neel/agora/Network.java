package com.neel.agora;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Network {
    public static NetworkResponse request(String url, RequestBody body, Context context, boolean isLogin) {
        NetworkResponse response;
        if (CredentialStorage.getInstance(context).getToken() == null){
            Network.login(context);
            response = request(url, body, context, isLogin);
        }
        else {
            response = Network.coreRequest(url, body, isLogin, context);
            if (!Network.isResponseValid(response)) {
                Log.d("Network", "invalid response");
                Network.login(context);
                response = request(url, body, context, isLogin);
            }
            Log.d("Network", "Valid response");
        }
        return response;
    }
    private static NetworkResponse coreRequest(String url, RequestBody body, boolean isLogin, Context context){
        NetworkResponse response = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder()
                              .url(new URL(url));

            if (body != null) {
                builder.post(body);
            }
            if (!isLogin) {
                builder.addHeader("X-Auth-Token", CredentialStorage.getInstance(context).getToken());
            }
            Request request = builder.build();
            response = new NetworkResponse(client.newCall(request).execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static boolean login(Context context){
        CredentialStorage credentialStorage = CredentialStorage.getInstance(context);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("identifier", credentialStorage.getUsername());
            jsonObject.put("password", credentialStorage.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        NetworkResponse response = coreRequest("https://agora-rest-api.herokuapp.com/api/v1/auth/login",body, true, context);
        JSONObject loginObject;
        try {
            loginObject = new JSONObject(response.getResponseString());
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            return  false;
        }
        UserHelper.makeNull();
        UserHelper.getUserHelper(loginObject, credentialStorage.getPassword());
        credentialStorage.setToken(UserHelper.getUserHelper().getToken());
        return true;
    }
    private static boolean isResponseValid(NetworkResponse response){
        if (response == null){
            return false;
        }
        else {
            try {
                String result = response.getResponseString();
                if (result.length() <= 0){
                    return false;
                }
                else {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("message") && jsonObject.getString("message").equals("Authentication required")){
                        return  false;
                    }
                }
            } catch (JSONException e) {
                return false;
            }

        }
        return true;
    }

    public static boolean areCredentialsStored(Context context){
        CredentialStorage credentialStorage = CredentialStorage.getInstance(context);
        return credentialStorage.getPassword() != null && credentialStorage.getUsername() != null;
    }
}
