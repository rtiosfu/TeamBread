package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.UserType;

import com.example.loginscreen.roomcode.User.*;
import com.example.loginscreen.roomcode.Room.*;
import com.example.loginscreen.roomcode.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button closeButton;
    EditText emailView;
    EditText passwordView;
    TextView incorrectAttempts;
    int attemptsLeft = 5;
    public static final String LOGIN_EXTRA = "com.example.loginscreen.LOGIN_ENTRY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button)findViewById(R.id.loginbutton);
        closeButton = (Button)findViewById(R.id.closebutton);
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText)findViewById(R.id.password);
        incorrectAttempts = (TextView)findViewById(R.id.incorrectAttempts);
        incorrectAttempts.setText(Integer.toString(attemptsLeft));

    }

    public void makeDirs(String name){
        File file = new File(getExternalFilesDir(null) + "/" +  name);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public void createDB(){
//        System.out.println("Copying");
        InputStream fStream = getResources().openRawResource(R.raw.testdataoldver);
        File file = new File(getExternalFilesDir(null) + "/testdataoldver.xls");
        makeDirs("classes");
        makeDirs("exams");
        makeDirs("professors");
        makeDirs("students");
        //only copy if the file does not already exist
        if(!(file.exists())) {
            System.out.println(getExternalFilesDir(null));
            try {
                FileOutputStream stream = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int read = 0;
                while ((read = fStream.read(buff)) > 0) {
                    stream.write(buff, 0, read);
                }
                stream.close();
                fStream.close();
                FileInputStream test = new FileInputStream(new File(getExternalFilesDir(null) + "/testdataoldver.xls"));
                System.out.println("Got here");
            } catch (IOException e) {
                System.out.println(e.getCause());
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //TODO write a function that checks email password combo for user in database
    //TODO populate class if email successfully logs in

    //Right now, checks an excel file to see if it has a matching email and then password
    //if login fails, returns null
    //if login succeeds, returns an associated class of either Professor, TA, or Student.
    ArrayList<String> checkLogin(String email, String password) {
//        ArrayList<String> stub = new ArrayList<String>;

        //Set up the workbook for the excel file
        HSSFWorkbook workbook = null;
        HSSFSheet sheet = null;
        try {
            File file = new File(getExternalFilesDir(null) + "/testdataoldver.xls");
            FileInputStream fStream = new FileInputStream(file);
            workbook = new HSSFWorkbook(fStream);
            sheet = workbook.getSheetAt(0);
            fStream.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
//
//        //Load in the excel file, hardcoded at the moment
//        //col 0 = firstname, col 1 = lastname, col 2 = Student #, col 3 = email, col 4 = type, col 5 = password
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rows; ++i){
                //This long conditional is basically just checking if the email and password match on the particular row.
            if(sheet.getRow(i).getCell(3).getStringCellValue().equals(email)){
                if( sheet.getRow(i).getCell(5).getStringCellValue().equals(password)){
                    String type = sheet.getRow(i).getCell(4).getStringCellValue();
                    //populate the array list with the needed information, refer to the col numbers for what they actually are.
                    switch(type){
                        case "STUDENT":
                            ArrayList<String> sInfo = new ArrayList<String>();
                            //add email
                            sInfo.add(sheet.getRow(i).getCell(3).getStringCellValue());
                            //add firstname and lastname
                            sInfo.add(sheet.getRow(i).getCell(0).getStringCellValue());
                            sInfo.add(sheet.getRow(i).getCell(1).getStringCellValue());
                            //add usertype
                            sInfo.add("STUDENT");
                            //add ID (and remove scientific format)
                            sInfo.add(String.format("%.0f", sheet.getRow(i).getCell(2).getNumericCellValue()));
                            return sInfo;

                        case "PROFESSOR":

                            ArrayList<String> pInfo = new ArrayList<String>();
                            //add email
                            pInfo.add(sheet.getRow(i).getCell(3).getStringCellValue());
                            //add firstname and lastname
                            pInfo.add(sheet.getRow(i).getCell(0).getStringCellValue());
                            pInfo.add(sheet.getRow(i).getCell(1).getStringCellValue());
                            //add usertype
                            pInfo.add("PROFESSOR");
                            //add ID (and remove scientific format)
                            pInfo.add(String.format("%.0f", sheet.getRow(i).getCell(2).getNumericCellValue()));
                            return pInfo;

                        case "TA":
                            ArrayList<String> tInfo = new ArrayList<String>();
                            //add email
                            tInfo.add(sheet.getRow(i).getCell(3).getStringCellValue());
                            //add firstname and lastname
                            tInfo.add(sheet.getRow(i).getCell(0).getStringCellValue());
                            tInfo.add(sheet.getRow(i).getCell(1).getStringCellValue());
                            //add usertype
                            tInfo.add("TA");
                            //add ID (and remove scientific format)
                            tInfo.add(String.format("%.0f", sheet.getRow(i).getCell(2).getNumericCellValue()));
                            return tInfo;
                    }
                }


                //want to populate a User class with the data if the login is successful.
            }
        }
//            reached the end of the database without finding a matching email.
            return null;
//        }
    }

//    public void onLogin(String email){
//        //TODO determine if email is associated with a TA, Prof, or Student and then fill the associated classes.
//        //note that the constructors request email, password, username, userType in that order.
//    }


    public void onLoginClick(View view){
        createDB();
        // TODO change the logic below to really check if a user can have their login attempt authenticated
        // currently just checks if email == "admin@" && password == "password" -> authentication
        emailView.setText(emailView.getText().toString().trim());

        // check if email has valid format
        if (!validateEmail(emailView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show(); // display error message
            return; // do not count this as an incorrect login attempt
        }
        ArrayList<String> userLogin = checkLogin(emailView.getText().toString(), passwordView.getText().toString());
        if (userLogin != null) {
            User user = new User();
//            Note, the return order will always be email, fname, lname, type, idnum
            //this is basically just constructing the user class with the proper parameters.
            switch(userLogin.get(3)){
                case "STUDENT":
                    user = new User(userLogin.get(0), userLogin.get(1), userLogin.get(2), userType.STUDENT, userLogin.get(4));
                    break;
                case "PROFESSOR":
                    user = new User(userLogin.get(0), userLogin.get(1), userLogin.get(2), userType.PROFESSOR, userLogin.get(4));
                    break;
                case "TA":
                    user = new User(userLogin.get(0), userLogin.get(1), userLogin.get(2), userType.TA, userLogin.get(4));
                    break;
            }

            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show(); // tell user that authentication complete
            if(user.type == userType.PROFESSOR){
                Intent intent = new Intent(com.example.loginscreen.MainActivity.this, createClassActivity.class);
                intent.putExtra(LOGIN_EXTRA, (Parcelable) user);
                startActivity(intent);
            }else{
                Intent intent = new Intent(com.example.loginscreen.MainActivity.this, enterRoomCode.class);
                intent.putExtra(LOGIN_EXTRA, (Parcelable) user);
                startActivity(intent);
            } //user is student

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

    public void onSignUpClick(View view){
        Intent intent = new Intent(com.example.loginscreen.MainActivity.this, signUpActivity.class);

        startActivity(intent);
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