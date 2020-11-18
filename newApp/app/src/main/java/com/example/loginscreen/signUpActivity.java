////////////////////////////////////////////
//TEAM BREAD
//signUpActivity.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.roomcode.User.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.example.loginscreen.MainActivity;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//The screen for the sign up page. Users may sign up for this service using an email and
//Other related information.
public class signUpActivity extends AppCompatActivity {

    public TextView email;
    public TextView fname;
    public TextView lname;
    public TextView pword;
    public TextView id;
    public RadioGroup typeButton;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct");
    private FirebaseAuth mAuth;

    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.signUpEmailEntry);
        fname = findViewById(R.id.signUpFirstNameEntry);
        lname = findViewById(R.id.signUpLastNameEntry);
        pword = findViewById(R.id.signUpPasswordEntry);
        id = findViewById(R.id.signUpStudentNumberEntry);
        typeButton = findViewById(R.id.userTypeRadioGroup);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        //Check if user is already signed in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    //Check if all of the fields have been filled and a radio button has been checked
    //Return true if email matches email pattern, and all of the other fields have something in them.
    //False otherwise.
    public boolean canSubmit(){
        return (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) &&
                !(fname.getText().toString().equals("")) &&
                !(lname.getText().toString().equals("")) &&
                !(pword.getText().toString().equals("")) &&
                !(id.getText().toString().equals("")) &&
                typeButton.getCheckedRadioButtonId() != -1;
    }

    //Returns the user type. TA is not included as it cannot be chosen during sign-up.
    public userType retUserType(){
        if(typeButton.getCheckedRadioButtonId() == R.id.userTypeStudentRB){
            return userType.STUDENT;
        }else{
            return userType.PROFESSOR;
        }
    }

    public boolean doesEmailMatch(){
        return false;
    }

    //Check if the email is already in the database, and then display a toast depending on the input.
    public void checkEmail(View view){
        if(doesEmailMatch()){
            Toast.makeText(getApplicationContext(), "Email is already in use. Please try another.", Toast.LENGTH_SHORT).show();
        }else if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "Email is OK.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Invalid email format.", Toast.LENGTH_SHORT).show();
        }
    }


    public void createAuthUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH_CREATE_SUCCESS", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH_CREATE_FAIL", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    public void createNewUser(String username, String fName, String lName, String email, String type,
                              String status, String classID, String examID) {
        DatabaseReference usernameRef = userRef.child("Users").child(username);
        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //the account already exists.
                    Toast.makeText(getApplicationContext(), "Account already taken.", Toast.LENGTH_SHORT).show();
                } else {
                    userRef.child("Users").child(username).child("fName").setValue(fName);
                    userRef.child("Users").child(username).child("lName").setValue(lName);
                    userRef.child("Users").child(username).child("type").setValue(type);
                    userRef.child("Users").child(username).child("status").setValue(status);
                    userRef.child("Users").child(username).child("email").setValue(email);
                    userRef.child("Users").child(username).child("classes").setValue("");
                    userRef.child("Users").child(username).child("exams").setValue("");

                    Toast.makeText(getApplicationContext(), "Account Created. Please log in.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database failure. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeToDatabase(){
        createNewUser(id.getText().toString().trim(), fname.getText().toString().trim(),
                lname.getText().toString().trim(), email.getText().toString().trim(), retUserType().toString(), "signed in",
                null, null);
    }

    //on user submit click.
    public void submitClick(View view){
        if(canSubmit()){
            //write user to database
            writeToDatabase();
            createAuthUser(email.getText().toString().trim(), pword.getText().toString());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Invalid entry. Please check your inputs.", Toast.LENGTH_SHORT).show(); // display error message
        }
    }
}