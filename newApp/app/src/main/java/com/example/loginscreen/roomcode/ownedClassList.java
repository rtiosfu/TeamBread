////////////////////////////////////////////
//TEAM BREAD
//ownedClassList.Java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.Room.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ownedClassList extends AppCompatActivity {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Classes");

    //TODO find owned classes via prof email.

    ArrayList<Room> classes = new ArrayList<Room>();
    RecyclerView rvClasses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_class_list);
        Intent intent = getIntent();
        classes = intent.getParcelableArrayListExtra(createClassActivity.CHECK_CLASS_EXTRA);

        rvClasses = findViewById(R.id.ownedClassList);
        classAdapter adapter = new classAdapter(classes);
        //basically a test implementation to see if we can catch which item was clicked.
        adapter.setOnItemClickListener(new classAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                //TODO be able to check associated exams.
                Toast.makeText(ownedClassList.this, position + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        rvClasses.setAdapter(adapter);
        rvClasses.setLayoutManager(new LinearLayoutManager(this));

    }


}