package com.neel.agora;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateElectionActivity extends AppCompatActivity{


    private ListView candidateListView;
    private EditText addCandidateEditText;
    private AddCandidateAdapter addCandidateAdapter;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Calendar startCal, endCal;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    String name = "", description = "", startDate = "", endDate = "";
    TextView votingAlgorithTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election);

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
                    votingAlgorithTextView.setText(selectedRadioButton.getText());
                }catch (Exception e){
                    Toast.makeText(CreateElectionActivity.this, "Kindly Select a Voting Algorithm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}
