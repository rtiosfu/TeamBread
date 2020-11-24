////////////////////////////////////////////
//TEAM BREAD
//enterRoomCode.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V3 CHANGES: None yet.
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import java.util.Calendar;
import java.util.Locale;

//TODO add way to check registered classes and exams, and then add a join button for exams.
//Screen for the Student to enter either a room code or class code. Displays student info as well.
public class enterRoomCode extends AppCompatActivity {
    //setup editable text and certain emails.
    Intent intent;
    String email;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    TextView classCode;
    TextView examRegCode;
    TextView examEnterCode;
    TextView emailView;
    TextView nameView;
    TextView typeView;
    TextView idView;
    TextView roomEntryClassName;
    String uEmail = "Logged in as: ";
    User user;
    public static final String EXAM_STUDENT_CLASS_EXTRA = "com.example.loginscreen.roomcode.User.enterRoomCode.EXAM_STUDENT_CLASS";
    public static final String EXAM_ENTRY_EXTRA = "com.example.loginscreen.roomcode.User.EXAM_ENTRY";
    private FusedLocationProviderClient fusedLocationClient;
    Location currentLoc = null;

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
        examEnterCode = findViewById(R.id.roomCodeEntry);
        examRegCode = findViewById(R.id.roomEntryRegisterExamCodeEntry);
        classCode = findViewById(R.id.classCodeEntry);
        roomEntryClassName = findViewById(R.id.roomEntryClassName);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLoc = location;
                }
            }
        };

        startLocationUpdates();

    }

    //Checks through a string of permissions to see if it is needed.
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


    //Requests location permissions if needed.
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

    private boolean requestingLocationUpdates = true;
    private LocationCallback locationCallback;

    //Location updates helper method. restarts location update after stopping (if requested)
    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private LocationRequest locationRequest;



    protected void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    //Note: all missing permission suppressions are taken care of by the reqPerm() function.
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        reqPerm();
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());

    }


    //get current location.
    @SuppressLint("MissingPermission")
    public void getLoc() {
        reqPerm();
//        startLocationUpdates();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLoc = location;
                        }
                    }
                });
    }

    //PARAMS: code is the class / exam code that the student has entered. roomSnap is a capture of the classes / exams
    // toReg is a reference to the classes or exams
    //path determines if we want to write to classes or exams. (For the user)
    public void updateUser(int code, DataSnapshot roomSnap, DatabaseReference toReg, String path){
//        startLocationUpdates();
//        getLoc();
        DatabaseReference userInDB = database.getReference("Proproct/Users/" + user.ID);
        String codeS = String.valueOf(code);
        userInDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child(path).hasChild(String.valueOf(code)))){
                    //add class to student and student to class
                    userInDB.child(path).child(codeS).setValue(roomSnap.child(codeS).child("Title").getValue());
                    toReg.child(codeS).child("Students").child(user.ID).child("email").setValue(user.email);
                    System.out.println(currentLoc.getLatitude());
                    System.out.println(currentLoc.getLongitude());
                    if(path.equals("exams") && currentLoc != null){
                        toReg.child(codeS).child("Students").child(user.ID).child("locLong").setValue(currentLoc.getLongitude());
                        toReg.child(codeS).child("Students").child(user.ID).child("locLat").setValue(currentLoc.getLatitude());
                    }
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
        int code = Integer.valueOf(classCode.getText().toString().trim());
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

    //Allows the user to enter the exam they have entered the code for if it is within the exam date
    //AND they are on the list (it is in their user class entry.)
    public void enterExam(View view){
        int code = Integer.valueOf(examEnterCode.getText().toString().trim());
        String codeS = String.valueOf(code);
        if(code > 100000000 && code < 999999999){
            DatabaseReference exams = database.getReference("Proproct/Exams/" + codeS);
            DatabaseReference userRef = database.getReference("Proproct/Users/" + user.ID + "/exams");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(codeS)){
                        exams.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot examSnapshot) {
                                int examDay = Integer.valueOf(examSnapshot.child("dateDay").getValue().toString());
                                int examYear = Integer.valueOf(examSnapshot.child("dateYear").getValue().toString());
                                int examMonth = Integer.valueOf(examSnapshot.child("dateMonth").getValue().toString());
                                int examHour = Integer.valueOf(examSnapshot.child("startTimeHour").getValue().toString());
                                int examMin = Integer.valueOf(examSnapshot.child("startTimeMin").getValue().toString());
                                int examLength = Integer.valueOf(examSnapshot.child("length").getValue().toString());
                                if (withinExamDate(examYear, examMonth, examDay, examHour, examMin, examLength)) {
                                    double regLat = (Double) examSnapshot.child("Students").child(user.ID).child("locLat").getValue();
                                    double regLong = (Double) examSnapshot.child("Students").child(user.ID).child("locLong").getValue();
                                    //TODO compare the current and last location.
                                    Location regLocation = new Location("");
                                    regLocation.setLatitude(regLat);
                                    regLocation.setLongitude(regLong);
                                    if (regLocation.distanceTo(currentLoc) <= 2000) {
                                        Intent intent = new Intent(com.example.loginscreen.roomcode.enterRoomCode.this, com.example.loginscreen.roomcode.examEntry.class);
                                        intent.putExtra(EXAM_ENTRY_EXTRA, user);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "You are too far from registered location..", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    //Do nothing, the toast is shown from the checking function.
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "You are not registered for this exam.", Toast.LENGTH_SHORT).show();
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

    //Checks if the current date and time is close enough to the provided exam dates.
    private boolean withinExamDate(int examYear, int examMonth, int examDay, int examTimeHour, int examTimeMin, int examLength){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(calendar.YEAR);
        //Dates are stored internally starting from 0. Go figure.
        int currentMonth = calendar.get(calendar.MONTH) + 1;
        int currentDay = calendar.get(calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(calendar.MINUTE);

//        System.out.println("EY = " + examYear + " EM " + examMonth + " ED " + examDay + " ETH " + examTimeHour + " ETM " + examTimeMin + " LEN " + examLength);
//        System.out.println("EY = " + currentYear + " EM " + currentMonth + " ED " + currentDay + " ETH " + currentHour + " ETM " + currentMin + " LEN " + examLength);

        //calculate minutes since the beginning of day.
        int examMin = (examTimeHour * 60) + examTimeMin;
        int currMin = (currentHour * 60) + currentMin;

        if(currentYear == examYear && currentMonth == examMonth && currentDay == examDay){
            if(currMin < examMin - 30){
                Toast.makeText(getApplicationContext(), "You are too early for the exam. Please come back within 30 minutes of the start time.", Toast.LENGTH_SHORT).show();
                return false;
            }else if(currMin > examMin + examLength){
                Toast.makeText(getApplicationContext(), "The exam is over.", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                return true;
            }
        }else{
            Toast.makeText(getApplicationContext(), "This is not the exam date.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    //Registers a student for an exam.
    public void sendExamCode(View view){
        int code = Integer.valueOf(examRegCode.getText().toString().trim());
        String codeS = String.valueOf(code);
        if (code > 100000000 && code < 999999999) {
                DatabaseReference exams = database.getReference("Proproct/Exams");
                exams.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //the code is in the database
                        if (snapshot.hasChild(codeS)) {
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
//                                            Intent intent = new Intent(com.example.loginscreen.roomcode.enterRoomCode.this, com.example.loginscreen.roomcode.examEntry.class);
//                                            startActivity(intent);
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Code.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){
                    }

                });

        }else{
            Toast.makeText(getApplicationContext(), "Invalid Code.", Toast.LENGTH_SHORT).show();
        }

    }
}




