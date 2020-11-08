package com.example.loginscreen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends Activity {
    public User user;

    Button loginButton; // attempt User login on click
    Button closeButton; // exit app on click
    Button createAccountButton; // create account on click
    EditText email;
    EditText password;
    TextView incorrectAttempts; // displays number of incorrect attempts
    int attemptsLeft = 5;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener fbAuthListener;
    private FirebaseAuth fbAuth;
    public FirebaseUser fbUser;

    // onCreate(Bundle) is used to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Overriden methods must call super.method()
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title bar
        setContentView(R.layout.activity_loginscreen); // get content settings from XML data

        fbAuth = FirebaseAuth.getInstance(); // initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        fbUser = fbAuth.getCurrentUser();

        user = new User();

        // find UI element view settings from XML data
        loginButton = (Button)findViewById(R.id.loginbutton);
        closeButton = (Button)findViewById(R.id.closebutton);
        createAccountButton = (Button)findViewById(R.id.createaccountbutton);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        incorrectAttempts = (TextView)findViewById(R.id.incorrectattempts);

        fbAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.v("TAG", "User signed in successfully");
                } else {
                    Log.v("TAG", "User not signed in");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set functionality for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "THANK YOU", Toast.LENGTH_SHORT).show();
                // TODO change the logic below to really check if a user can have their login attempt authenticated
                // currently just checks if email == "admin@" && password == "password" -> authentication

                // check if email has valid format
                if (!validateEmail(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show(); // display error message
                    email.setText("");
                    password.setText("");
                    return; // do not count this as an incorrect login attempt
                }

                if (email.getText().toString().equals("admin@") && password.getText().toString().equals("password")) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show(); // tell user that authentication complete
                } else {
                    attemptsLeft--;
                    incorrectAttempts.setText(Integer.toString(attemptsLeft));
                    if (attemptsLeft == 0) {
                        loginButton.setEnabled(false); // disable login button if user has entered attemptsLeft incorrect attempts
                    }
                }
                user = new User(email.getText().toString(), password.getText().toString());
                email.setText("");
                password.setText("");
                Toast.makeText(getApplicationContext(), user.email + " " + user.password, Toast.LENGTH_SHORT).show();
                // TODO check if email + password combo matches a User in the database
                signIn(user.email, user.password);
            }
        });

        // set functionality for close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // finish tasks, call onDestroy() methods
                System.exit(0); // exit with status 0 (NO ERROR)
            }
        });

        // set functionality for createAccount button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show(); // display error message
                    return; // do not count this as an incorrect login attempt
                }
                createAccount(email.getText().toString(), password.getText().toString());
                signIn(email.getText().toString(), password.getText().toString());
                user.email = email.getText().toString();
                user.password = password.getText().toString();
                getCurrentUser();
                Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Log.v("HEY", user.email + " " + user.password);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fbAuth.addAuthStateListener(fbAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fbAuthListener != null) {
            fbAuth.removeAuthStateListener(fbAuthListener);
        }
    }

    // return true if email has valid format, false if email does not.
    public boolean validateEmail(String email) {
        // if email contains ' ' -> DO NOT AUTHENTICATE, INVALID EMAIL
        if (email.contains(" ")) {
            return false;
        }
        // if email does not contain exactly one '@' -> DO NOT AUTHENTICATE, INVALID EMAIL
        if (countCharOccurrences(email, "@") != 1) {
            return false;
        }
        return true; // all tests passed
    }

    // return the number of times (char) x appears in str
    public int countCharOccurrences(String str, String x) {
        int ret = 0; // return value
        for (int i = 0; i < str.length(); i++) {
            // if the characters match -> increment return value
            if (Character.toString(str.charAt(i)).equals(x)) {
                ret++;
            }
        }
        return ret;
    }

    // create new Firebase account
    public void createAccount(String email, String password) {
        fbAuth.createUserWithEmailAndPassword(email, password);
    }

    // sign users in
    public void signIn(String email, String password) {
        fbAuth.signInWithEmailAndPassword(email, password);
    }

    // get user information
    public void getCurrentUser() {
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}
