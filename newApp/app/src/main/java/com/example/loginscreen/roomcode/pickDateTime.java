////////////////////////////////////////////
//TEAM BREAD
//pickDateTime.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.loginscreen.R;

import java.util.Calendar;

//Test class. unused in the final app.
public class pickDateTime extends AppCompatActivity {

    TextView parentExamTimeHour;
    TextView parentExamTimeMin;
    TextView parentExamDay;
    TextView parentExamMonth;
    TimePicker simpleTimePicker;
    DatePicker simpleDatePicker;
    TextView parentExamYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date_time);
        parentExamTimeHour = findViewById(R.id.pickHour);
        parentExamTimeMin = findViewById(R.id.pickMin);
        parentExamDay = findViewById(R.id.pickDay);
        parentExamMonth = findViewById(R.id.pickMonth);
//        parentExamYear = findViewById(R.id.);
        simpleTimePicker = findViewById(R.id.activitySimpleTimePicker);
        simpleDatePicker = findViewById(R.id.activitySimpleDatePicker);
        // perform set on time changed listener event
        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                System.out.println(hourOfDay + " " + minute);
                String h = Integer.toString(hourOfDay);
                String m = Integer.toString(minute);
                parentExamTimeHour.setText(h);
                parentExamTimeMin.setText(m);
            }
        });
        Calendar calendar = Calendar.getInstance();
        simpleDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                parentExamYear.setText(Integer.toString(year));
                parentExamMonth.setText(Integer.toString(month));
                parentExamDay.setText(Integer.toString(dayOfMonth));

            }
        });
    }





}