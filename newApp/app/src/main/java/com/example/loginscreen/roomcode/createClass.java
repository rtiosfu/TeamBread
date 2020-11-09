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

public class createClass extends AppCompatActivity {

    public static int generateRandomDigits() {
        int m = (int) Math.pow(10, 9 - 1);
        return m + new Random().nextInt(9 * m);
    }

    public void createLookup(){
        try {

            FileOutputStream out = new FileOutputStream(lookup);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet();
            wb.setSheetName(0, "Sheet1");
            int row = 0;
            Row r = s.createRow(0);
            String[] toInsert = {"id", "class", "professor"};
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
            wb.write(out);
            out.close();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public boolean lookupCode(int search){
        return false;
    }

    public void writeClassRegistry(String id, String cName, String profName){
        //indexes 0 = id, 1 = class title, 2 = professor
        File registry = new File(getExternalFilesDir(null) + "/classes/" + id + ".xls");
        try{

            FileOutputStream out = new FileOutputStream(registry);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet();
            wb.setSheetName(0, "Sheet1");
            int row = 0;
            Row r = s.createRow(0);
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

    public void writeClassToDB(Object[] toInsert){
        try {
            FileInputStream in = new FileInputStream(lookup);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int row = sheet.getLastRowNum();
            Row r = sheet.createRow(row + 1);
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
                            int randCode = generateRandomDigits();
                            code.setText(Integer.toString(randCode));
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