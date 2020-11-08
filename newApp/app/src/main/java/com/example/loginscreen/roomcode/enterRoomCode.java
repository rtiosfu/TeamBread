package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
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
            for(int i = 1; i <= rows; i++){
//                System.out.println(i);
                if (String.format("%.0f", sheet.getRow(i).getCell(0).getNumericCellValue()).equals(code)){
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

    //implement a dialog asking if they wish to join
    public void sendClassCode(View view){
        int row = lookupCode(classCode.getText().toString());
        if(row > 0);
//        System.out.println(lookupCode(classCode.getText().toString()));
    }

    public void sendExamCode(View view){
        Intent intent = new Intent(this, com.example.loginscreen.roomcode.examEntry.class);
        startActivity(intent);
    }

}