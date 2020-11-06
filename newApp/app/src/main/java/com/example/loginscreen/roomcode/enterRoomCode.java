package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;



public class enterRoomCode extends AppCompatActivity {
    Intent intent;
    String email;
    TextView testString;
    String uEmail = "Logged in as: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room_code);
        intent = getIntent();
        testString = findViewById(R.id.testString);
        email = intent.getStringExtra(MainActivity.LOGIN_EXTRA);
        testString.setText(uEmail + email);
    }

}