package com.neel.agora.Election;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ElectionData {
    public static String id;
    public static String name;
    public static String description;
    public static String creatorName;
    public static String creatorEmail;
    public static String start;
    public static String end;
    public static Boolean realtimeResult;
    public static String votingAlgo;
    public static JSONArray candidates;
    public static String ballotVisibility;
    public static Boolean voterListVisibility;
    public static Boolean isInvite;
    public static Boolean isCompleted;
    public static Boolean isStarted;
    public static String createdTime;
    public static String adminLink;
    public static String inviteCode;
    public static JSONArray ballot;
    public static JSONArray voterList;
    public static JSONArray winners;
    public static Boolean isCounted;
    public static Integer noVacancies;
    public static Boolean isActive = false;
    public static Boolean isFinished = false;
    public static Boolean isPending = false;

    public ElectionData(JSONObject jsonObject){
        try {
            id = jsonObject.getString("_id");
            name = jsonObject.getString("name");
            description = jsonObject.getString("description");
            creatorName = jsonObject.getString("creatorName");
            creatorEmail = jsonObject.getString("creatorEmail");
            start = jsonObject.getString("start");
            end = jsonObject.getString("end");
            realtimeResult = jsonObject.getBoolean("realtimeResult");
            votingAlgo = jsonObject.getString("votingAlgo");
            candidates = jsonObject.getJSONArray("candidates");
            ballotVisibility = jsonObject.getString("ballotVisibility");
            voterListVisibility = jsonObject.getBoolean("voterListVisibility");
            isInvite = jsonObject.getBoolean("isInvite");
            isCompleted = jsonObject.getBoolean("isCompleted");
            isStarted = jsonObject.getBoolean("isStarted");
            createdTime = jsonObject.getString("createdTime");
            adminLink = jsonObject.getString("adminLink");
            inviteCode = jsonObject.getString("inviteCode");
            ballot = jsonObject.getJSONArray("ballot");
            voterList = jsonObject.getJSONArray("voterList");
            winners = jsonObject.getJSONArray("winners");
            isCounted = jsonObject.getBoolean("isCounted");
            noVacancies = jsonObject.getInt("noVacancies");

            TimeZone tz = TimeZone.getTimeZone("Australia/Sydney");
            Calendar startCal = Calendar.getInstance(tz);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setCalendar(startCal);
            startCal.setTime(sdf.parse(start));

            Calendar endCal = Calendar.getInstance(tz);
            sdf.setCalendar(endCal);
            endCal.setTime(sdf.parse(end));

            if (startCal.after(Calendar.getInstance(tz))) {
                isPending = true;
                isActive = false;
                isFinished = false;
            }
            else if (endCal.before(Calendar.getInstance(tz))) {
                isFinished = true;
                isPending = false;
                isActive = false;
            }
            else {
                isActive = true;
                isPending = false;
                isFinished = false;
            }

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
