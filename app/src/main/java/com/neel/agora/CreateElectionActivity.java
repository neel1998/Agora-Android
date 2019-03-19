package com.neel.agora;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateElectionActivity extends AppCompatActivity{


    private ViewPager createElectionPager;
    private int[] layouts = {R.layout.create_election_layout1, R.layout.create_election_layout2, R.layout.create_election_layout3, R.layout.create_election_layout4, R.layout.create_election_layout5};
    private View currentView;
    private int currentItem;
    private Context context;
    private TextView pageCountTextView;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Calendar startCal, endCal;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    String name = "", description = "", startDate = "", endDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election);

        context = this;
        createElectionPager = findViewById(R.id.create_election_pager);
        CreateElectionPagerAdapter createElectionPagerAdapter = new CreateElectionPagerAdapter(layouts, this);
        createElectionPager.setAdapter(createElectionPagerAdapter);


        View datePickerView = LayoutInflater.from(this).inflate(layouts[1], null, false);

        Button nextButton = findViewById(R.id.next_btn);
        Button prevButton = findViewById(R.id.prev_btn);
        pageCountTextView = findViewById(R.id.create_election_page_count);

        currentItem = createElectionPager.getCurrentItem();

        currentView = LayoutInflater.from(CreateElectionActivity.this).inflate(layouts[createElectionPager.getCurrentItem()],null, false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = createElectionPager.getCurrentItem();
                int newPage = currentItem;
                switch (currentItem){
                    case 0:
                        EditText electionNameEdit = findViewById(R.id.create_election_name);
                        EditText electionDesEdit  = findViewById(R.id.create_election_description);
                        if (electionDesEdit.getText().toString().length() == 0 || electionNameEdit.getText().toString().length() == 0) {
                            Toast.makeText(context, "Kindly fill the necessary details", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            name = electionNameEdit.getText().toString();
                            description = electionDesEdit.getText().toString();
                            newPage += 1;
                        }
                        break;
                    case 1:
                        if (startDate.length() == 0 || endDate.length() == 0) {
                            Toast.makeText(context, "Kindly fill the necessary details", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            newPage += 1;
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                pageCountTextView.setText(String.valueOf(newPage + 1) + "/" + String.valueOf(layouts.length));
                createElectionPager.setCurrentItem(newPage, true);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = createElectionPager.getCurrentItem();
                int newPage = currentItem - 1;
                pageCountTextView.setText(String.valueOf(newPage + 1) + "/" + String.valueOf(layouts.length));
                createElectionPager.setCurrentItem(newPage , true);
            }
        });
        createElectionPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
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
}
