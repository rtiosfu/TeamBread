package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.MainActivity;
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


public class enterRoomCode extends AppCompatActivity {
    Intent intent;
    String email;
    TextView classCode;
    TextView examCode;
    TextView emailView;
    TextView nameView;
    TextView typeView;
    TextView idView;
    TextView roomEntryClassName;
    String uEmail = "Logged in as: ";
    User user;
    public static final String EXAM_STUDENT_CLASS_EXTRA = "com.example.loginscreen.roomcode.User.enterRoomCode.EXAM_STUDENT_CLASS";
    File classLookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room_code);
        intent = getIntent();
        emailView = findViewById(R.id.emailString);
        nameView = findViewById(R.id.nameString);
        typeView = findViewById(R.id.typeString);
        idView = findViewById(R.id.idString);
        user = intent.getParcelableExtra(MainActivity.LOGIN_EXTRA);
        emailView.setText("User email: " + user.email);
        nameView.setText("User name: " + user.username);
        typeView.setText("User type: " + user.type);
        idView.setText("User ID: " + String.valueOf(user.ID));
        classLookup = new File(getExternalFilesDir(null) + "/classes/lookup.xls");
        examCode = findViewById(R.id.roomCodeEntry);
        classCode = findViewById(R.id.classCodeEntry);
        roomEntryClassName = findViewById(R.id.roomEntryClassName);
    }

    //returns true if the code is in the lookup table.
    public int lookupCode(String code){
        if(Integer.parseInt(code) < 10000000){
            return -1;
        }
        try {
            FileInputStream in = new FileInputStream(classLookup);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int rows = sheet.getLastRowNum();
            int codeCol = 0;
            int classCol = 1;
            for(int i = 1; i <= rows; i++){
//                System.out.println(i);
                if (String.format("%.0f", sheet.getRow(i).getCell(codeCol).getNumericCellValue()).equals(code)){
                    roomEntryClassName.setText(sheet.getRow(i).getCell(classCol).getStringCellValue());
                    return i;
                }
            }
            workbook.close();
        }catch (IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public void writeToDB(HSSFWorkbook wb, HSSFSheet s, File registry){
//        File registry = new File(getExternalFilesDir(null) + "/classes/" + classCode.getText().toString() + ".xls");
        try{
            FileOutputStream out = new FileOutputStream(registry);
            wb.write(out);
            out.close();
            wb.close();
        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerStudent(int enteredCode, boolean forClasses){
        try {
            File reg;
            if(forClasses) {
                reg = new File(getExternalFilesDir(null) + "/classes/" + classCode.getText().toString() + ".xls");
            }else{
                reg = new File(getExternalFilesDir(null) + "/classes/" + examCode.getText().toString() + ".xls");
            }
            FileInputStream in = new FileInputStream(reg);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int row = 1;
            int maxRow = sheet.getLastRowNum();
            int studentCol = 0;
            for(row = row; row <= maxRow; row++){
                Row checkRow = sheet.getRow(row);
                if(checkRow != null){
                    Cell c = checkRow.getCell(studentCol);
                    //if the cell is blank
                    if(c == null || c.getCellType() == Cell.CELL_TYPE_BLANK){
                        c = checkRow.createCell(studentCol);
                        c.setCellValue(user.email);

                        writeToDB(workbook, sheet, reg);
                        return;
                    }
                }
            }//reached maxRow with filled cells
            maxRow++;
            Row r = sheet.createRow(maxRow);
            Cell c = r.createCell(studentCol);
            String addEmail = user.email.trim();
            c.setCellValue(addEmail);
            writeToDB(workbook, sheet, reg);
            return;

        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //implement a dialog asking if they wish to join
    public void sendClassCode(View view){
        int row = lookupCode(classCode.getText().toString());

        if(row > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("Do you wish to join class: '" + roomEntryClassName.getText().toString().trim() + "' ?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerStudent(Integer.parseInt(classCode.getText().toString()), true);
                            Toast.makeText(getApplicationContext(), "Registered.", Toast.LENGTH_SHORT).show();
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
//        System.out.println(lookupCode(classCode.getText().toString()));
    }

    public void sendExamCode(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examEntry.class);
        startActivity(intent);
    }

}