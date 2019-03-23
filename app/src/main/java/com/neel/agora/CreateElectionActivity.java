package com.neel.agora;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreateElectionActivity extends AppCompatActivity{


    private ListView candidateListView;
    private EditText addCandidateEditText;
    private ProgressBar createElectionProgBar;
    private Button createElectionButton;
    private AddCandidateAdapter addCandidateAdapter;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Calendar startCal, endCal;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    String name = "", description = "", startDate = "", endDate = "", votingAlgo = "";
    TextView votingAlgorithTextView;
    JSONArray candidates = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election);

        createElectionButton = findViewById(R.id.create_election_button);
        createElectionProgBar = findViewById(R.id.create_election_progress_bar);
        candidateListView = findViewById(R.id.create_election_candidate_listview);
        addCandidateEditText = findViewById(R.id.create_election_candidate_name);
        votingAlgorithTextView = findViewById(R.id.voting_algo_value);
        addCandidateAdapter = new AddCandidateAdapter(this, new ArrayList<String>());
        candidateListView.setAdapter(addCandidateAdapter);
        candidateListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(candidateListView);
    }

    public void selectEndDate(View view){
        final EditText endEditText = (EditText)view;
        endCal = Calendar.getInstance();
        endDatePickerDialog = new DatePickerDialog(CreateElectionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCal.set(year, month, dayOfMonth);
                endTimePickerDialog = new TimePickerDialog(CreateElectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endCal.set(Calendar.MINUTE, minute);
                        endDate = format.format(endCal.getTimeInMillis());
                        endEditText.setText(endDate);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE), true );
                endTimePickerDialog.show();
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
        endDatePickerDialog.show();

    }

    public void selectStartDate(View view) {
        final EditText startEditText = (EditText)view;
        startCal = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(CreateElectionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCal.set(year, month, dayOfMonth);
                startTimePickerDialog = new TimePickerDialog(CreateElectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        startCal.set(Calendar.MINUTE, minute);
                        startDate = format.format(startCal.getTimeInMillis());
                        startEditText.setText(startDate);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE), true );
                startTimePickerDialog.show();
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
        startDatePickerDialog.show();

    }
    public void addCandidate(View view) {
        addCandidateEditText = findViewById(R.id.create_election_candidate_name);
        String candidateName = addCandidateEditText.getText().toString();
        if (candidateName.length() == 0) {
            Toast.makeText(this, "Please Enter Candidate's name", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d("name", candidateName);
            addCandidateAdapter.add(candidateName);
            candidateListView.setAdapter(addCandidateAdapter);
            candidates.put(candidateName);
            setListViewHeightBasedOnChildren(candidateListView);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    public void selectVotingAlgorithm(View view){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.voting_algo_alert_layout, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RadioGroup votingAlgoRadioGroup = dialogView.findViewById(R.id.voting_algo_radio_group);
                try{
                    RadioButton selectedRadioButton = dialogView.findViewById(votingAlgoRadioGroup.getCheckedRadioButtonId());
                    votingAlgo = selectedRadioButton.getText().toString();
                    votingAlgorithTextView.setText(votingAlgo);
                }catch (Exception e){
                    Toast.makeText(CreateElectionActivity.this, "Kindly Select a Voting Algorithm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public void createElection(View view) {
        view.setVisibility(View.GONE);
        createElectionProgBar.setVisibility(View.VISIBLE);
        new CreateElectionTask().execute();
    }
    private class CreateElectionTask extends AsyncTask<Void, Void, NetworkResponse>{
        @Override
        protected NetworkResponse doInBackground(Void... voids) {
            NetworkResponse response = null;
            EditText nameEditText = findViewById(R.id.create_election_name);
            EditText desEditText = findViewById(R.id.create_election_description);
            CheckBox realTimeCheckBox = findViewById(R.id.real_time_check_box);
            CheckBox inviteVotersCheckBox = findViewById(R.id.invite_voters_check_box);
            RadioButton secretBallotRadio1 = findViewById(R.id.secret_ballot_radio1);
            RadioButton secretBallotRadio2 = findViewById(R.id.secret_ballot_radio2);
            RadioButton secretBallotRadio3 = findViewById(R.id.secret_ballot_radio3);
            RadioButton voterListRadio1 = findViewById(R.id.voter_list_visible_radio1);
            RadioButton voterListRadio2 = findViewById(R.id.voter_list_visible_radio2);
            boolean isValid = true;
            name = nameEditText.getText().toString();
            description = desEditText.getText().toString();

            if (name == null || name.length() == 0) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Enter Election Name", Toast.LENGTH_SHORT).show();
            }
            else if (description == null || description.length() == 0){
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Enter Election Description", Toast.LENGTH_SHORT).show();
            }
            else if (startDate == null || startDate.length() == 0) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Election Start Date", Toast.LENGTH_SHORT).show();
            }
            else if (endDate == null || endDate.length() == 0) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Election End Date", Toast.LENGTH_SHORT).show();
            }
            else if (candidates.length() == 0) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Atleast One Candidate", Toast.LENGTH_SHORT).show();
            }
            else if (votingAlgo == null || votingAlgo.length() == 0) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Election Voting Algorithm", Toast.LENGTH_SHORT).show();
            }
            else if (!secretBallotRadio1.isChecked() && !secretBallotRadio2.isChecked() && !secretBallotRadio3.isChecked()) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Anyone option for secrecy of ballots", Toast.LENGTH_SHORT).show();
            }
            else if (!voterListRadio1.isChecked() && !voterListRadio2.isChecked()) {
                isValid = false;
                Toast.makeText(CreateElectionActivity.this, "Kindly Select Anyone option for visibility of Voters List", Toast.LENGTH_SHORT).show();
            }
            if (isValid) {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                JSONObject ballotObject = new JSONObject();
                JSONArray ballotArray = new JSONArray();
                try {
                    jsonObject.put("name", name);
                    jsonObject.put("description", description);
                    jsonObject.put("candidates", candidates);
                    jsonObject.put("ballotVisibility", "temp");
                    jsonObject.put("voterListVisibility", !voterListRadio1.isChecked());
                    jsonObject.put("startingDate", startDate);
                    jsonObject.put("endingDate", endDate);
                    jsonObject.put("isInvite", inviteVotersCheckBox.isChecked());
                    jsonObject.put("isRealTime", realTimeCheckBox.isChecked());
                    jsonObject.put("votingAlgo", votingAlgo);
                    jsonObject.put("noVacancies", 0);
                    ballotObject.put("voteBallot", "temp");
                    ballotObject.put("voterEmail", "temp");
                    ballotArray.put(ballotObject);
                    jsonObject.put("ballot", ballotArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                response = Network.request("https://agora-rest-api.herokuapp.com/api/v1/election", body, CreateElectionActivity.this, false);
            }
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            if (response != null) {
                Toast.makeText(CreateElectionActivity.this, response.getResponseString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateElectionActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Log.d("create Election", "Success");
            }
            else {
                createElectionButton.setVisibility(View.VISIBLE);
                createElectionProgBar.setVisibility(View.GONE);
                Log.d("create Election", "Failed");
            }
        }
    }
}
