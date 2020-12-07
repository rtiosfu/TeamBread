////////////////////////////////////////////
//TEAM BREAD
//createClassActivity.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: Added check exams button.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.Room.Room;
import com.example.loginscreen.roomcode.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


//This screen is essentially a navigation screen that allows a professor user to
//Create a class
//Create an exam
//Check existing classes and exams
public class createClassActivity extends AppCompatActivity {
    //extra keys
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference exams = database.getReference("Proproct/Exam");
    DatabaseReference classes = database.getReference("Proproct/Classes");
    public static final String CREATE_CLASS_EXTRA = "com.example.loginscreen.roomcode.CREATE_CLASS_USER";
    public static final String CREATE_EXAM_EXTRA = "com.example.loginscreen.roomcode.CREATE_EXAM_USER";
    public static final String CHECK_CLASS_EXTRA = "com.example.loginscreen.roomcode.CHECK_CLASS_USER";
    public static final String CHECK_EXAM_EXTRA = "com.example.loginscreen.roomcode.CHECK_EXAM_USER";

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


    public void onCheckExamClick(View view){
        ArrayList<Room> owned = new ArrayList();
        DatabaseReference nClass = database.getReference("Proproct/Users/" + user.ID + "/exams");
        nClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot c : snapshot.getChildren()){
                        owned.add(new Room(c.getValue().toString(), c.getKey()));

                }
                Intent intent = new Intent(createClassActivity.this, com.example.loginscreen.roomcode.ownedExamList.class);
                intent.putParcelableArrayListExtra(CHECK_EXAM_EXTRA, owned);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onCheckClassClick(View view){
        ArrayList<Room> owned = new ArrayList();
        DatabaseReference nExam = database.getReference("Proproct/Users/" + user.ID + "/classes");
        nExam.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot c : snapshot.getChildren()){
                    owned.add(new Room(c.getValue().toString(), c.getKey()));
                }

                Intent intent = new Intent(createClassActivity.this, com.example.loginscreen.roomcode.ownedClassList.class);
                intent.putParcelableArrayListExtra(CHECK_CLASS_EXTRA, owned);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}