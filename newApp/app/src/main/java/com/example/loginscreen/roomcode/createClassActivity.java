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
    public static final String CREATE_CLASS_EXTRA = "com.example.loginscreen.roomcode.USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
    }

    User user;
    public void onCreateClassClick(View view){
        Intent intent = getIntent();
        user = intent.getParcelableExtra(MainActivity.LOGIN_EXTRA);
        intent = new Intent(this, com.example.loginscreen.roomcode.createClass.class);
        intent.putExtra(CREATE_CLASS_EXTRA, user);
        startActivity(intent);
    }

}