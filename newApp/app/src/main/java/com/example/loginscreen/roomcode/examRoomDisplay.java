////////////////////////////////////////////
//TEAM BREAD
//examRoomDisplay.Java
//PROGRAMMERS: Ryan
//KNOWN BUGS: This is basically a stub, and as such doesn't have any of the required functionality.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginscreen.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class examRoomDisplay extends AppCompatActivity {

    private TextView timeRemainingView;
    BroadcastReceiver timeReceiver;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    String endHourS = "7";
    String endMinS = "10";
    int endHour = 21;
    int endMin = 10;
    int endTime = endHour * 60 + endMin;


    private void getExamEndTime(){
        //TODO get exam end time from database
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_room_display);

        timeRemainingView = findViewById(R.id.examRoomTimeRemaining);

        Calendar calendar = Calendar.getInstance();


        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currMin = calendar.get(Calendar.MINUTE);
        int currTime = currHour * 60 + currMin;

        int diff = endTime - currTime;

        String diffHour = String.format(Locale.ENGLISH, "%02d", diff / 60);
        diff = diff % 60;

//        String currHourS = String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.HOUR_OF_DAY));
//        String currMinS = String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.MINUTE));



        timeRemainingView.setText(diffHour + ":" + diff);

    }

    @Override
    public void onStart(){
        super.onStart();

        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0){
                    Calendar calendar = Calendar.getInstance();

                    int currHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int currMin = calendar.get(Calendar.MINUTE);
                    int currTime = currHour * 60 + currMin;

                    int diff = endTime - currTime;

                    String diffHour = String.format(Locale.ENGLISH, "%02d", diff / 60);
                    diff = diff % 60;

                    timeRemainingView.setText(diffHour + ":" + diff);


                }
//                    timeRemainingView.setText(dateFormat.format(new Date()));
            }
        };

        registerReceiver(timeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop(){
        super.onStop();

        if(timeReceiver != null){
            unregisterReceiver(timeReceiver);
        }
    }
}