package com.neel.agora;

import java.io.IOException;

import okhttp3.Response;

public class NetworkResponse {
    private Response mReponse;
    private String mResponseString;
    public NetworkResponse(Response response){
        mReponse = response;
        try {
            mResponseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Response getReponse() {
        return  mReponse;
    }
    public String getResponseString() {
        return mResponseString;
    }
}
