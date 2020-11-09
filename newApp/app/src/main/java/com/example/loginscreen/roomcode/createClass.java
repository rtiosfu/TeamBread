////////////////////////////////////////////
//TEAM BREAD
//createClass.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: Generated room code may be a duplicate. Drastically unlikely, but not guaranteed not to be.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

//Screen that allows a professor to create a class
//Providing a class name.
public class createClass extends AppCompatActivity {

    //Generates and returns a random 10 digit code.
    public static int generateRandomDigits() {
        int m = (int) Math.pow(10, 9 - 1);
        return m + new Random().nextInt(9 * m);
    }


    //Create the lookup.xls file if it does not already exist.
    public void createLookup(){
        try {
            //open an output stream
            FileOutputStream out = new FileOutputStream(lookup);
            //setup the excel sheet
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet();
            wb.setSheetName(0, "Sheet1");
            int row = 0;
            Row r = s.createRow(0);
            String[] toInsert = {"id", "class", "professor"};
            int col = 0;
            //insert the fields into the spreadsheet.
            for(Object field : toInsert) {
                Cell cell = r.createCell(col);
                col++;
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                }else if(field instanceof Integer){
                    cell.setCellValue((Integer) field);
                }
            }
            wb.write(out);
            out.close();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    //Return true if the code parameter is in the lookup file.
    public boolean lookupCode(int search){
        return false;
    }


    //write the created class to a separate excel file.
    //the file will be contained in the class folder, naming convention classcode.xls
    public void writeClassRegistry(String id, String cName, String profName){

        //indexes 0 = id, 1 = class title, 2 = professor
        File registry = new File(getExternalFilesDir(null) + "/classes/" + id + ".xls");
        try{
            //open file for output
            FileOutputStream out = new FileOutputStream(registry);
            //create the new excel spreadsheet
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet();
            wb.setSheetName(0, "Sheet1");
            int row = 0;
            Row r = s.createRow(0);
            //template for the top row
            String[] toInsert = {"registered", "owner", "TA", "cname"};
            int col = 0;
            for(Object field : toInsert) {
                Cell cell = r.createCell(col);
                col++;
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                }else if(field instanceof Integer){
                    cell.setCellValue((Integer) field);
                }
            }
            //add the prof email
            r = s.createRow(1);
            Cell cell = r.createCell(1);
            cell.setCellValue(profName);
            //add the class name
            cell = r.createCell(3);
            cell.setCellValue(cName);
            wb.write(out);
            out.close();
            wb.close();
    }catch(IOException e){
        Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
        System.out.println(e.getCause());
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    }

    //Isolated function to handle writing the class details to the lookup folder. Will call a separate function to create
    //the class xls file.
    //The parameter is the data that will be added to the first free row in the lookup file.
    public void writeClassToDB(Object[] toInsert){
        try {
            //open the lookup file
            FileInputStream in = new FileInputStream(lookup);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int row = sheet.getLastRowNum();
            Row r = sheet.createRow(row + 1);
            int col = 0;
            //insert each item in the list parameter.
            for(Object field : toInsert) {
                Cell cell = r.createCell(col);
                col++;
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                }else if(field instanceof Integer){
                    cell.setCellValue((Integer) field);
                }
            }
            FileOutputStream out = new FileOutputStream(lookup);
            workbook.write(out);
            workbook.close();
            writeClassRegistry(code.getText().toString(), classTitle.getText().toString(), user.email);
        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
                            int randCode = generateRandomDigits();
                            //set the TextView text to the random code.
                            code.setText(Integer.toString(randCode));
                            //the array structure is [code, class title, prof email]
                            Object[] toInsert = {randCode, classTitle.getText().toString(), user.email};
                            Toast.makeText(getApplicationContext(), "Class has been created.", Toast.LENGTH_SHORT).show();
                            writeClassToDB(toInsert);
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

    //setup a few variables.
    File lookup;
    TextView classTitle;
    TextView code;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class2);
        Intent intent = getIntent();
        user = intent.getParcelableExtra(createClassActivity.CREATE_CLASS_EXTRA);
        lookup = new File(getExternalFilesDir(null) + "/classes/lookup.xls");
        classTitle = (TextView)findViewById(R.id.createExamRoomNameEntry);
        code = (TextView)findViewById(R.id.createExamRoomGeneratedCode);
        if(!lookup.exists()){
            createLookup();
        }
    }
}