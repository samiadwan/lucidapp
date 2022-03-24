package com.lucid.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{


    TaskDBHelper mydb;
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    String dateFinal;
    String nameFinal;
    String timeFinal;
    String locationFinal;
    Intent intent;
    Boolean isUpdate;
    String id;
    EditText task_name, task_time, task_date, task_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);

        mydb = new TaskDBHelper(getApplicationContext());
        intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);

        task_name = findViewById(R.id.task_name);
        task_date = findViewById(R.id.task_date);
        task_time = findViewById(R.id.task_time);
        task_location = findViewById(R.id.task_location);


        dateFinal = todayDateString();
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        if (isUpdate) {
            init_update();
        }
    }



    public void init_update() {
        id = intent.getStringExtra("id");
        TextView toolbar_task_add_title = (TextView) findViewById(R.id.toolbar_task_add_title);
        toolbar_task_add_title.setText("Update");
        Cursor task = mydb.getDataSpecific(id);
        if (task != null) {
            task.moveToFirst();

            task_name.setText(task.getString(1).toString());
            Calendar cal = Function.Epoch2Calender(task.getString(2).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);
            task_date.setText(Function.Epoch2DateString(task.getString(2).toString(), "dd/MM/yyyy"));
            task_time.setText(task.getString(3).toString());
            task_location.setText(task.getString(4).toString());


        }

    }

    public String todayDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());

        return dateFormat.toString();

    }


    public void closeAddTask(View v) {
        finish();
    }


    public void doneAddTask(View v) {
        int errorStep = 0;
        EditText task_name = (EditText) findViewById(R.id.task_name);
        nameFinal = task_name.getText().toString();
        dateFinal = task_date.getText().toString();
        timeFinal = task_time.getText().toString();
        locationFinal = task_location.getText().toString();


        /* Checking */
        if (nameFinal.trim().length() < 1) {
            errorStep++;
            task_name.setError("Provide a task name.");
        }

        if (dateFinal.trim().length() < 4) {
            errorStep++;
            task_date.setError("Provide a specific date");
        }


        if (errorStep == 0) {
            if (isUpdate) {
                mydb.updateContact(id, nameFinal, dateFinal, timeFinal, locationFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertContact(nameFinal, dateFinal, timeFinal, locationFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    public void showStartTimePicker(View v) {

        tpd = TimePickerDialog.newInstance(AddTask.this, 0, 0, false);
        tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                String hourString, apm="AM";
                if (hourOfDay > 12) {
                    hourOfDay = (hourOfDay-12);
                    apm="PM";
                }
                hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String secondString = second < 10 ? "0" + second : "" + second;
                task_time.setText(hourString + ":" + minuteString+" "+apm);
            }
        });

        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void showStartDatePicker(View v) {

        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(AddTask.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                int monthAddOne = startMonth + 1;
                String date = (startDay < 10 ? "0" + startDay : "" + startDay) + "/" +
                        (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) + "/" +
                        startYear;
                task_date.setText(date);
            }
        });
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }



    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    }


    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }




    /*public void showStartDatePicker(View v) {
        dpd = DatePickerDialog.newInstance(AddTask.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
    }*/

    /*public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }*/

}