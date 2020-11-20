////////////////////////////////////////////
//TEAM BREAD
//enterRoomCode.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.example.loginscreen.roomcode.User.User;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

//TODO add way to check registered classes and exams, and then add a join button for exams.
//Screen for the Student to enter either a room code or class code. Displays student info as well.
public class enterRoomCode extends AppCompatActivity {
    //setup editable text and certain emails.
    Intent intent;
    String email;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
    private FusedLocationProviderClient fusedLocationClient;

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
        examCode = findViewById(R.id.roomCodeEntry);
        classCode = findViewById(R.id.classCodeEntry);
        roomEntryClassName = findViewById(R.id.roomEntryClassName);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
//    private ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // features requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            });

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void reqPerm(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
//            return;
        }
    }

    @SuppressLint("MissingPermission")
    public void getLoc(View view) {
        reqPerm();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            System.out.println(location.getLatitude());
                            System.out.println(location.getLongitude());
                        }
                        System.out.println("Here!@");
                    }
                });


    }

    //PARAMS: code is the class / exam code that the student has entered. roomSnap is a capture of the classes / exams
    // toReg is a reference to the classes or exams
    //path determines if we want to write to classes or exams.
    public void updateUser(int code, DataSnapshot roomSnap, DatabaseReference toReg, String path){
        DatabaseReference userInDB = database.getReference("Proproct/Users/" + user.ID);
        String codeS = String.valueOf(code);
        userInDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child(path).hasChild(String.valueOf(code)))){
                    //add class to student and student to class
                    userInDB.child(path).child(codeS).setValue(roomSnap.child(codeS).child("Title").getValue());
                    toReg.child(codeS).child("Students").child(user.ID).setValue(user.email);
                    Toast.makeText(getApplicationContext(), "Registered.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //implement a dialog asking if they wish to join, only if the code is already in the database.
    public void sendClassCode(View view) {
//        int row = lookupCode(classCode.getText().toString());
        int code = Integer.valueOf(classCode.getText().toString());
        String codeS = String.valueOf(code);
        if (code > 100000000 && code < 999999999) {
            DatabaseReference classes = database.getReference("Proproct/Classes");
            classes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //the code is in the database
                    if(snapshot.hasChild(codeS)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.loginscreen.roomcode.enterRoomCode.this);
                        builder.setCancelable(true);
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Do you wish to join class: '" + snapshot.child(codeS).child("Title").getValue() + "' ?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //register the student
                                        updateUser(code, snapshot, classes, "classes");
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
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "Invalid Code.", Toast.LENGTH_SHORT).show();
        }
    }

    //Takes student to a sample entry exam page. Will be implemented further with exam code checking.
    public void sendExamCode(View view){
        int code = Integer.valueOf(examCode.getText().toString());
        String codeS = String.valueOf(code);
        if (code > 100000000 && code < 999999999) {
                DatabaseReference exams = database.getReference("Proproct/Exams");
                exams.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //the code is in the database
                        if(snapshot.hasChild(codeS)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(com.example.loginscreen.roomcode.enterRoomCode.this);
                            builder.setCancelable(true);
                            builder.setTitle("Are you sure?");
                            builder.setMessage("Do you wish to join exam: '" + snapshot.child(codeS).child("Title").getValue() + "' ?");
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //register the student
                                            updateUser(code, snapshot, exams, "exams");
                                            Intent intent = new Intent(com.example.loginscreen.roomcode.enterRoomCode.this, com.example.loginscreen.roomcode.examEntry.class);
                                            startActivity(intent);
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
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        }else{
            Toast.makeText(getApplicationContext(), "Invalid Code.", Toast.LENGTH_SHORT).show();
        }

    }
}




