package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;


public class enterRoomCode extends AppCompatActivity {
    Intent intent;
    String email;
    TextView emailView;
    TextView nameView;
    TextView typeView;
    TextView idView;
    String uEmail = "Logged in as: ";
    User user;
    public static final String EXAM_STUDENT_CLASS_EXTRA = "com.example.loginscreen.roomcode.User.enterRoomCode.EXAM_STUDENT_CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room_code);
        intent = getIntent();
        emailView = findViewById(R.id.emailString);
        nameView = findViewById(R.id.nameString);
        typeView = findViewById(R.id.typeString);
        idView = findViewById(R.id.idString);
        user = intent.getParcelableExtra(MainActivity.LOGIN_EXTRA);
        emailView.setText("User email: " + user.email);
        nameView.setText("User name: " + user.username);
        typeView.setText("User type: " + user.type);
        idView.setText("User ID: " + String.valueOf(user.ID));
    }

    public void sendClassCode(View view){
//        Intent intent = new Intent(this)
    }

    public void sendExamCode(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examEntry.class);
        startActivity(intent);
    }

}