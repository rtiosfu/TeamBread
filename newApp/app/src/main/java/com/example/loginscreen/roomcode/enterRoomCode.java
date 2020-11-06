package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;



public class enterRoomCode extends AppCompatActivity {
    Intent intent;
    String email;
    TextView emailView;
    String uEmail = "Logged in as: ";
    public static final String EXAM_STUDENT_CLASS_EXTRA = "com.example.loginscreen.roomcode.User.enterRoomCode.EXAM_STUDENT_CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room_code);
        intent = getIntent();
        emailView = findViewById(R.id.emailString);
        email = intent.getStringExtra(MainActivity.LOGIN_EXTRA);
        emailView.setText(uEmail + email);
    }

    public void sendClassCode(View view){
//        Intent intent = new Intent(this)
    }

    public void sendExamCode(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examEntry.class);
        startActivity(intent);
    }

}