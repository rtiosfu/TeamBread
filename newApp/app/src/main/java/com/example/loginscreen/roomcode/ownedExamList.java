////////////////////////////////////////////
//TEAM BREAD
//ownedExamList.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.Room.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//basically almost exactly the same as ownedClassList.
public class ownedExamList extends AppCompatActivity {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Exams");

    //TODO find owned classes via prof email.

    ArrayList<Room> exams = new ArrayList<Room>();
    RecyclerView rvClasses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_class_list);
        Intent intent = getIntent();
        exams = intent.getParcelableArrayListExtra(createClassActivity.CHECK_EXAM_EXTRA);

        rvClasses = findViewById(R.id.ownedClassList);
        examClassAdapter adapter = new examClassAdapter(exams);
        adapter.setOnItemClickListener(new examClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                //TODO be able to check associated exams.
                Toast.makeText(ownedExamList.this, position + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        rvClasses.setAdapter(adapter);
        rvClasses.setLayoutManager(new LinearLayoutManager(this));

    }


}