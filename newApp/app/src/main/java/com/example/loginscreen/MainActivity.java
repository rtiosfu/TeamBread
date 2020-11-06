package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;
import com.example.loginscreen.roomcode.User;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.roomcode.enterRoomCode;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button closeButton;
    EditText email;
    EditText password;
    TextView incorrectAttempts;
    int attemptsLeft = 5;
    public static final String LOGIN_EXTRA = "com.example.loginscreen.LOGIN_ENTRY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button)findViewById(R.id.loginbutton);
        closeButton = (Button)findViewById(R.id.closebutton);
        email = (EditText) findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        incorrectAttempts = (TextView)findViewById(R.id.incorrectAttempts);

    }

    public void onLogin(String email){
        //TODO determine if email is associated with a TA, Prof, or Student and then fill the associated classes.
        //note that the constructors request email, password, username, userType in that order.
    }


    public void onLoginClick(View view){
        // TODO change the logic below to really check if a user can have their login attempt authenticated
        // currently just checks if email == "admin@" && password == "password" -> authentication

        // check if email has valid format
        if (!validateEmail(email.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show(); // display error message
            return; // do not count this as an incorrect login attempt
        }
        // TODO check if email + password combo matches a User in the database
        if (email.getText().toString().equals("admin@") && password.getText().toString().equals("password")) {
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show(); // tell user that authentication complete
            Intent intent = new Intent(com.example.loginscreen.MainActivity.this, enterRoomCode.class);
            String successEmail = email.getText().toString();
            intent.putExtra(LOGIN_EXTRA, successEmail);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show(); // display error message
            attemptsLeft--; // decrement counter of remaining attempts
            incorrectAttempts.setText(Integer.toString(attemptsLeft)); // display number of attempts remaining
            if (attemptsLeft == 0) {
                loginButton.setEnabled(false); // disable login button if user has entered attemptsLeft incorrect attempts
            }
        }
    }

    public void onCloseClick(View view){
        finish(); //Finish tasks & call destructors
        System.exit(0); // exit with status 0
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
}