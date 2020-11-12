////////////////////////////////////////////
//TEAM BREAD
//enterRoomCode.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
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
import java.util.Locale;


//Screen for the Student to enter either a room code or class code. Displays student info as well.
public class enterRoomCode extends AppCompatActivity {
    //setup editable text and certain emails.
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
        System.out.println(user.email);
        emailView.setText("User email: " + user.email);
        nameView.setText("User name: " + user.username);
        typeView.setText("User type: " + user.type);
        idView.setText("User ID: " + String.valueOf(user.ID));
        //This particular file is the lookup file, containing ids associated with class names.
        classLookup = new File(getExternalFilesDir(null) + "/classes/lookup.xls");
        examCode = findViewById(R.id.roomCodeEntry);
        classCode = findViewById(R.id.classCodeEntry);
        roomEntryClassName = findViewById(R.id.roomEntryClassName);
    }

    //returns true if the code is in the lookup table.
    public int lookupCode(String code){
        //checks if the code is acceptable.
        code.trim();
        if(code.length() != 9){
            return -1;
        }
        try {
            //open class lookup table
            FileInputStream in = new FileInputStream(classLookup);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int rows = sheet.getLastRowNum();
            int codeCol = 0;
            int classCol = 1;
            for(int i = 1; i <= rows; i++){
                //Checking if the code in the row equals the code provided by the user.
                if (String.format(Locale.ENGLISH, "%.0f", sheet.getRow(i).getCell(codeCol).getNumericCellValue()).equals(code)){
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

    //Helper function for registerStudent, basically just writes the wb parameter to the registry File parameter.
    public void writeToDB(HSSFWorkbook wb, HSSFSheet s, File registry){
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

    //register the student into either the class or exam. forClasses is the check that determines if they are trying to get into a class or exam.
    public void registerStudent(int enteredCode, boolean forClasses){
        try {
            File reg;
            //open the given class code or exam code file.
            if(forClasses) {
                reg = new File(getExternalFilesDir(null) + "/classes/" + classCode.getText().toString() + ".xls");
            }else{
                reg = new File(getExternalFilesDir(null) + "/exams/" + examCode.getText().toString() + ".xls");
            }
            FileInputStream in = new FileInputStream(reg);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheetAt(0);
            in.close();
            int row = 1;
            int maxRow = sheet.getLastRowNum();
            int studentCol = 0;
            for(; row <= maxRow; row++){
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
            //returns from here

        }catch(IOException e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //implement a dialog asking if they wish to join, only if the code is already in the database.
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
        }else{
            Toast.makeText(getApplicationContext(), "Invalid Code.", Toast.LENGTH_SHORT).show();
        }
//        System.out.println(lookupCode(classCode.getText().toString()));
    }

    //Takes student to a sample entry exam page. Will be implemented further with exam code checking.
    //TODO add exam code database comparison.
    public void sendExamCode(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examEntry.class);
        startActivity(intent);
    }

}