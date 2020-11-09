package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.loginscreen.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class createRoom extends AppCompatActivity {

    //Set up all of the editable text boxes.
    TextView examTimeHour;
    TextView examTimeMin;
    TextView examYear;
    TextView examMonth;
    TextView examDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        examTimeHour = findViewById(R.id.examCreateRoomTimeHour);
        examTimeMin = findViewById(R.id.examCreateRoomTimeMin);
        examYear = findViewById(R.id.examCreateRoomDayYear);
        examMonth = findViewById(R.id.examCreateRoomDayMonth);
        examDay = findViewById(R.id.examCreateRoomDay);
        Calendar calendar = Calendar.getInstance();
        //set each box to the current date and time.
        examYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        examMonth.setText(Integer.toString(calendar.get(Calendar.MONTH)));
        examDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
        examTimeHour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        examTimeMin.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
    }

    //TODO Add functionality to create lookup file, create class files, and update.

}