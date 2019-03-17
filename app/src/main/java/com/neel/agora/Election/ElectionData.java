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
    private String id;
    private String name;
    private String description;
    private String creatorName;
    private String creatorEmail;
    private String start;
    private String end;
    private Boolean realtimeResult;
    private String votingAlgo;
    private JSONArray candidates;
    private String ballotVisibility;
    private Boolean voterListVisibility;
    private Boolean isInvite;
    private Boolean isCompleted;
    private Boolean isStarted;
    private String createdTime;
    private String adminLink;
    private String inviteCode;
    private JSONArray ballot;
    private JSONArray voterList;
    private JSONArray winners;
    private Boolean isCounted;
    private Integer noVacancies;
    private Boolean isActive = false;
    private Boolean isFinished = false;
    private Boolean isPending = false;
    private String status = null;
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
                status = "Pending";
                isPending = true;
                isActive = false;
                isFinished = false;
            }
            else if (endCal.before(Calendar.getInstance(tz))) {
                status = "Finished";
                isFinished = true;
                isPending = false;
                isActive = false;
            }
            else {
                status = "Active";
                isActive = true;
                isPending = false;
                isFinished = false;
            }

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Boolean getRealtimeResult() {
        return realtimeResult;
    }

    public String getVotingAlgo() {
        return votingAlgo;
    }

    public JSONArray getCandidates() {
        return candidates;
    }

    public String getBallotVisibility() {
        return ballotVisibility;
    }

    public Boolean getVoterListVisibility() {
        return voterListVisibility;
    }

    public Boolean getIsInvite() {
        return isInvite;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getAdminLink() {
        return adminLink;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public JSONArray getBallot() {
        return ballot;
    }

    public JSONArray getVoterList() {
        return voterList;
    }

    public JSONArray getWinners() {
        return winners;
    }

    public Boolean getIsCounted() {
        return isCounted;
    }

    public Integer getNoVacancies() {
        return noVacancies;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public Boolean getIsPending() {
        return isPending;
    }
}
