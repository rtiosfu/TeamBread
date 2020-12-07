////////////////////////////////////////////
//TEAM BREAD
//examRoomDisplay.Java
//PROGRAMMERS: Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: Created in V3.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//A display of the exam room once the student has entered. Has time remaining, start time, and end time.
public class examRoomDisplay extends AppCompatActivity {

    private TextView timeRemainingView;
    BroadcastReceiver timeReceiver;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    User user;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    String endHourS = "7";
    String endMinS = "10";
    int endHour = 21;
    int endMin = 10;
    int endTime = endHour * 60 + endMin;
    TextView endTimeView;
    TextView showStartTime;
    TextView showEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_room_display);

        endTimeView = findViewById(R.id.examRoomStartTimeView);
        timeRemainingView = findViewById(R.id.examRoomTimeRemaining);
        showStartTime = findViewById(R.id.examRoomStartTime);
        showEndTime = findViewById(R.id.examRoomEndTime);
        Intent intent = getIntent();
        String code = intent.getStringExtra(enterRoomCode.EXAM_ENTRY_CODE);
//        System.out.println(code);
        DatabaseReference exams = database.getReference("Proproct/Exams/" + code);
        //Get the end times from the database and calculate the remaining time accordingly.
        exams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                endHour = Integer.valueOf(snapshot.child("startTimeHour").getValue().toString());
                endMin = Integer.valueOf(snapshot.child("startTimeMin").getValue().toString());
                int length = Integer.valueOf(snapshot.child("length").getValue().toString());
//                timeRemainingView.setText(String.format("%d : %d", endHour, endMin));
                endTime = endHour * 60 + endMin + length;

                showStartTime.setText(String.format("%d : %2d", endHour, endMin));
                showEndTime.setText(String.format("%d : %2d", endTime / 60, endTime % 60));

                Calendar calendar = Calendar.getInstance();

                int currHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currMin = calendar.get(Calendar.MINUTE);
                int currTime = currHour * 60 + currMin;

                int diff = endTime - currTime;

                String diffHour = String.format(Locale.ENGLISH, "%02d", diff / 60);
                diff = diff % 60;
                timeRemainingView.setText(diffHour + ":" + diff);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //setting up receivers to update the timer every minute.
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

    //deregister receiver once it is no longer needed.
    @Override
    public void onStop(){
        super.onStop();

        if(timeReceiver != null){
            unregisterReceiver(timeReceiver);
        }
    }
}