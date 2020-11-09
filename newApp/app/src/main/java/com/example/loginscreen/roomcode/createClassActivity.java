package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.*;
import com.example.loginscreen.roomcode.User.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class createClassActivity extends AppCompatActivity {
    //extra keys
    public static final String CREATE_CLASS_EXTRA = "com.example.loginscreen.roomcode.CREATE_CLASS_USER";
    public static final String CREATE_EXAM_EXTRA = "com.example.loginscreen.roomcode.CREATE_EXAM_USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        Intent intent = getIntent();
        user = intent.getParcelableExtra(MainActivity.LOGIN_EXTRA);
    }

    User user;
    //This function will be called when the user clicks 'Create Class'.
    //It will call the 'createClass' activity and display that screen to the user.
    public void onCreateClassClick(View view){

        Intent intent = new Intent(this, com.example.loginscreen.roomcode.createClass.class);
        intent.putExtra(CREATE_CLASS_EXTRA, user);
        startActivity(intent);

    }

    //This function will be called when the user clicks 'Create Exam'.
    //It will call the 'createRoom' activity and display that screen to the user.
    public void onCreateExamClick(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.createRoom.class);
        intent.putExtra(CREATE_EXAM_EXTRA, user);
        startActivity(intent);
    }



}