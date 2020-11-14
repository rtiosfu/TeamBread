////////////////////////////////////////////
//TEAM BREAD
//createClassActivity.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//TODO Add functionality for the 'check rooms' button that shows the professor their existing rooms
//This screen is essentially a navigation screen that allows a professor user to
//Create a class
//Create an exam
//Check existing classes and exams
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
        Intent intent = new Intent(this, createExam.class);
        intent.putExtra(CREATE_EXAM_EXTRA, user);
        startActivity(intent);
    }
}