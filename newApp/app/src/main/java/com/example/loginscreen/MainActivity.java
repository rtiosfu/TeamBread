////////////////////////////////////////////
//TEAM BREAD
//MainActivity.Java
//PROGRAMMERS:Ryan, Dylan
//KNOWN BUGS:Textboxes to not reset after signing up.
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.roomcode.User.*;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button closeButton;
    EditText emailView;
    EditText passwordView;
    EditText IDView;
    TextView incorrectAttempts;


    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference userRef = database.getReference("Proproct");
    private FirebaseAuth mAuth;
    int attemptsLeft = 5;
    public static final String LOGIN_EXTRA = "com.example.loginscreen.LOGIN_ENTRY";
    User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginbutton);
        closeButton = findViewById(R.id.closebutton);
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        incorrectAttempts = findViewById(R.id.incorrectAttempts);
        incorrectAttempts.setText(String.format(Locale.ENGLISH, "%d", attemptsLeft));
        IDView = findViewById(R.id.mainStudentID);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
//                    System.out.println(location.getLongitude());
//                    System.out.println(location.getLatitude());
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is already signed in.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO maybe add an updateUI
    }




    //Attempts to login using the current email and password entered into the text fields.
    //Shows a toast on success or failure.
    public void attemptLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN_SUCCESS", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authentication Success",
                                    Toast.LENGTH_SHORT).show();
                            finishLogin(emailView.getText().toString().trim(), IDView.getText().toString().trim());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN_FAIL", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //This creates the user class and essentially just cleans up after the main chunk of login.
    public void finishLogin(String email, String idNum) {
        userRef.child("Users").child(idNum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TODO add handling if ID number doesn't match.
//                System.out.println(snapshot.child("fName").getValue());
                String fName = Objects.requireNonNull(snapshot.child("fName").getValue()).toString();
                String lName = Objects.requireNonNull(snapshot.child("lName").getValue()).toString();
                userType uType = userType.valueOf(Objects.requireNonNull(snapshot.child("type").getValue()).toString());
                currentUser = new User(email, fName, lName, uType, idNum);
                emailView.setText("");
                passwordView.setText("");
                IDView.setText("");
                //Start the roomcodeentry activity
                if(currentUser.type == userType.STUDENT) {
                    Intent intent = new Intent(MainActivity.this, com.example.loginscreen.roomcode.enterRoomCode.class);
                    intent.putExtra(LOGIN_EXTRA, currentUser);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, com.example.loginscreen.roomcode.createClassActivity.class);
                    intent.putExtra(LOGIN_EXTRA, currentUser);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        System.out.println(currentUser.email);

    }

    public void onLoginClick(View view) {
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString();
        attemptLogin(email, password);
    }

    //Close the app gracefully.
    public void onCloseClick(View view) {
        finish(); //Finish tasks & call destructors
        System.exit(0); // exit with status 0
    }

    //When the user clicks the sign up button, take them to the sign up page.
    public void onSignUpClick(View view) {
        Intent intent = new Intent(com.example.loginscreen.MainActivity.this, signUpActivity.class);

        startActivity(intent);
    }


    // return true if email has valid format, false if email does not.
//    public boolean validateEmail(String email) {
//        // if email contains ' ' -> DO NOT AUTHENTICATE, INVALID EMAIL
//        if (email.contains(" ")) {
//            return false;
//        }
//        // if email does not contain exactly one '@' -> DO NOT AUTHENTICATE, INVALID EMAIL
//        if (countCharOccurrences(email, "@") != 1) {
//            return false;
//        }
//        return true; // all tests passed
//    }
    // return the number of times (char) x appears in str
//    public int countCharOccurrences(String str, String x) {
//        int ret = 0; // return value
//        for (int i = 0; i < str.length(); i++) {
//            // if the characters match -> increment return value
//            if (Character.toString(str.charAt(i)).equals(x)) {
//                ret++;
//            }
//        }
//        return ret;
//    }
}