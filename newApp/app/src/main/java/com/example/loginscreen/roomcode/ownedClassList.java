package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ownedClassList extends AppCompatActivity {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct/Classes");

    //TODO find owned classes via prof email.

    ArrayList<String> classes = new ArrayList<String>();
    RecyclerView rvClasses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_class_list);
        rvClasses = findViewById(R.id.ownedClassList);
        classes.add("Test1");
        classes.add("Test2");
        classes.add("Test3");
        classAdapter adapter = new classAdapter(classes);
        adapter.setOnItemClickListener(new classAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String test = classes.get(position);
                Toast.makeText(ownedClassList.this, test + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        rvClasses.setAdapter(adapter);
        rvClasses.setLayoutManager(new LinearLayoutManager(this));

    }


}