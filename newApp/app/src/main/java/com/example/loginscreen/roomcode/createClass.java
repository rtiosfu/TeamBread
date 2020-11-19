////////////////////////////////////////////
//TEAM BREAD
//createClass.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: Generated room code may be a duplicate. Drastically unlikely, but not guaranteed not to be.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

//Screen that allows a professor to create a class
//Providing a class name.
public class createClass extends AppCompatActivity {

    //setup a few variables.
    TextView classTitle;
    TextView classCode;
    User user;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference classRef = database.getReference("Proproct/Classes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class2);
        Intent intent = getIntent();
        user = intent.getParcelableExtra(createClassActivity.CREATE_CLASS_EXTRA);
        classTitle = (TextView)findViewById(R.id.examCreateRoomNameEntry);
        classCode = (TextView)findViewById(R.id.examRoomGeneratedCode);
    }


    //Generates and returns a random 10 digit code.
    public static int generateRandomDigits() {
        int m = (int) Math.pow(10, 9 - 1);
        return m + new Random().nextInt(9 * m);
    }

    //returns true if the class was created, false otherwise.
    public void createClassinDB(){

        DatabaseReference classIDRef = classRef;
        DatabaseReference userClassRef = database.getReference("Proproct/Users/" + user.ID + "/classes");
        classIDRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int prospectiveCode = generateRandomDigits();
                //Handle the (unlikely) case that the same ID is generated.
                while(snapshot.hasChild(String.valueOf(prospectiveCode))){
                    //This should only happen ~ 1 / 999999999 times, but this handles dupes.
                    prospectiveCode = generateRandomDigits();
                }

                //TODO maybe add a creation date? So that it can be deleted after a certain time.
                String code = String.valueOf(prospectiveCode);
                classRef.child(code).child("Students").setValue("");
                classRef.child(code).child("Owner").setValue(user.email);
                classRef.child(code).child("Profname").setValue(user.username);
                classRef.child(code).child("TAs").setValue("");
                classRef.child(code).child("Title").setValue(classTitle.getText().toString().trim());
                userClassRef.child(code).setValue(classTitle.getText().toString().trim());
                classCode.setText(code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database failure. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //When the create button is clicked, a prompt will display to the user that confirms their intent to create a class.
    public void onCreateClick(View view){
        //8 digit code
        if(classTitle.getText().toString().trim().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("Do you wish to create class: '" + classTitle.getText().toString().trim() + "' ?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //The user has clicked yes.
                            //set the TextView text to the random code.
                            //the array structure is [code, class title, prof email]
                            createClassinDB();
                            Toast.makeText(getApplicationContext(), "Class has been created.", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing on cancel
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}