package com.neel.agora.Election;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    private static ArrayList<ElectionData> mTotalElectionDataList;
    private static ArrayList<ElectionData> mActiveElectionDataList;
    private static ArrayList<ElectionData> mPendingElectionDataList;
    private static ArrayList<ElectionData> mFinishedElectionDataList;
    public static void getElectionData(final AsyncCallback<int[]> callback, Context context){
        mContext = context;
        mTotalElectionDataList = new ArrayList<>();
        mActiveElectionDataList = new ArrayList<>();
        mPendingElectionDataList = new ArrayList<>();
        mFinishedElectionDataList = new ArrayList<>();
        ElectionTask electionTask = new ElectionTask(){
            @Override
            protected void onPostExecute(NetworkResponse response) {
                if (response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.getResponseString());
                        if (jsonObject.has("elections")) {
                            JSONArray electionDataArray = jsonObject.getJSONArray("elections");
                            for (int i = 0; i < electionDataArray.length(); i ++){
                                ElectionData data = new ElectionData(electionDataArray.getJSONObject(i));
                                mTotalElectionDataList.add(data);
                                if (data.getIsFinished()) {
                                   Log.d(data.getName(), "finished");
                                   mFinishedElectionDataList.add(data);
                                }
                                if (data.getIsPending()) {
                                    Log.d(data.getName(), "pending");
                                    mPendingElectionDataList.add(data);
                                }
                                if(data.getIsActive()) {
                                    Log.d(data.getName(), "active");
                                    mActiveElectionDataList.add(data);
                                }
                            }
                            callback.success(new int[]{mTotalElectionDataList.size(),mPendingElectionDataList.size(),mActiveElectionDataList.size(),mFinishedElectionDataList.size()});
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

    public static ArrayList<ElectionData> getActiveElectionDataList() {
        return mActiveElectionDataList;
    }

    public static ArrayList<ElectionData> getPendingElectionDataList() {
        return mPendingElectionDataList;
    }

    public static ArrayList<ElectionData> getFinishedElectionDataList() {
        return mFinishedElectionDataList;
    }

    public static ArrayList<ElectionData> getTotalElectionDataList() {
        return mTotalElectionDataList;
    }
}
