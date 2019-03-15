package com.neel.agora.Election;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.neel.agora.AsyncCallback;
import com.neel.agora.Network;
import com.neel.agora.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ElectionDataAsyncTask {
    private static Context mContext;
    private static ArrayList<ElectionData> mElectionDataList;
    public static void getElectionData(final AsyncCallback<int[]> callback, Context context){
        mContext = context;
        mElectionDataList = new ArrayList<>();
        ElectionTask electionTask = new ElectionTask(){
            @Override
            protected void onPostExecute(NetworkResponse response) {
                if (response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.getResponseString());
                        if (jsonObject.has("elections")) {
                            JSONArray electionDataArray = jsonObject.getJSONArray("elections");
                            for (int i = 0; i < electionDataArray.length(); i ++){
                                mElectionDataList.add(new ElectionData(electionDataArray.getJSONObject(i)));
                            }
                            callback.success(new int[]{mElectionDataList.size(),0,0,0});
                        }else {
                            callback.error(new IOException());
                        }
                    } catch (JSONException e) {
                        callback.error(e);
                    }
                }else{
                    callback.error(new IOException());
                }
            }
        };
        electionTask.execute();
    }
    private static class ElectionTask extends AsyncTask<Void, Void, NetworkResponse>{
        @Override
        protected NetworkResponse doInBackground(Void... voids) {
            return Network.request("https://agora-rest-api.herokuapp.com/api/v1/election", null, mContext, false);
        }
    }

    public static ArrayList<ElectionData> getElectionDataList() {
        return mElectionDataList;
    }
}
