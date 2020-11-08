package com.example.loginscreen.roomcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.loginscreen.R;

import java.io.File;
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

        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "Error opening database. Please try again.", Toast.LENGTH_SHORT).show();
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    String lookup = getExternalFilesDir(null) + "/classes/lookup.xls";
    File look = new File(lookup);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class2);
        if(!look.exists()){
            createLookup();
        }
    }
}