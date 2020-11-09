////////////////////////////////////////////
//TEAM BREAD
//signUpActivity.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.roomcode.User.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


//The screen for the sign up page. Users may sign up for this service using an email and
//Other related information.
public class signUpActivity extends AppCompatActivity {

    TextView email;
    TextView fname;
    TextView lname;
    TextView pword;
    TextView id;
    RadioGroup typeButton;


    //Check if all of the fields have been filled and a radio button has been checked
    //Return true if email matches email pattern, and all of the other fields have something in them.
    //False otherwise.
    public boolean canSubmit(){
//        System.out.println(typeButton.getCheckedRadioButtonId());
//        System.out.println(typeButton.getCheckedRadioButtonId() == R.id.userTypeProfessorRB);
//        System.out.println(typeButton.getCheckedRadioButtonId() == R.id.userTypeStudentRB);
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

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = (TextView)findViewById(R.id.signUpEmailEntry);
        fname = (TextView)findViewById(R.id.signUpFirstNameEntry);
        lname = (TextView)findViewById(R.id.signUpLastNameEntry);
        pword = (TextView)findViewById(R.id.signUpPasswordEntry);
        id = (TextView)findViewById(R.id.signUpStudentNumberEntry);
        typeButton = (RadioGroup)findViewById(R.id.userTypeRadioGroup);

    }

    //Check if the email matches any in the database.
    //Returns true if so, false if not.
    public boolean doesEmailMatch(){
        HSSFWorkbook workbook = null;
        HSSFSheet sheet = null;
        try {
            //open the test data file.
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
        int currRows = sheet.getLastRowNum();
        int emailCol = 3;
        String testEmail = email.getText().toString();
        //check through the column of emails against the inputted email.
        for(int i = 1; i <=currRows; i++){
            if(sheet.getRow(i).getCell(emailCol).getStringCellValue().equals(testEmail)){
                try {
                    workbook.close();
                    return true;
                }catch (IOException e){
                    Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getCause());
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

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

    //Write sign up information to the database.
    public void writeToDatabase(User u){
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
        //create an arraylist to insert.
        Object[] toInsert = {fname.getText().toString(), lname.getText().toString(), Integer.parseInt(u.ID), u.email, u.type.toString(), pword.getText().toString()};
        int currRows = sheet.getLastRowNum();
//        System.out.println(currRows);
        Row row = sheet.createRow(++currRows);

        int col = 0;
        //Insert the above list into the row of the excel sheet.
        for(Object field : toInsert){
            Cell cell = row.createCell(col);
            col++;
            if(field instanceof String){
                cell.setCellValue((String) field);
            }else if (field instanceof Integer){
                cell.setCellValue((Integer) field);
            }
        }

        try {
            //write to the file.
            FileOutputStream outputStream = new FileOutputStream(getExternalFilesDir(null) + "/testdataoldver.xls");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }catch(FileNotFoundException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    //on user submit click.
    public void submitClick(View view){
        if(canSubmit()){
            //create user class from the data in the text boxes.
            User user = new User(email.getText().toString().trim(), fname.getText().toString().trim(),
                                lname.getText().toString().trim(), retUserType(),
                                id.getText().toString().trim());
            //write user to database
            writeToDatabase(user);
            Toast.makeText(getApplicationContext(), "Account Created. Please log in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Invalid entry. Please check your inputs.", Toast.LENGTH_SHORT).show(); // display error message
        }
    }
}