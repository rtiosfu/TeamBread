////////////////////////////////////////////
//TEAM BREAD
//createExam.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None
//V3 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

import static com.example.loginscreen.roomcode.createClass.generateRandomDigits;


//Screen that allows a Professor user to create an exam room by specifying Class,
//start and end time, as well as exam title.
public class createExam extends AppCompatActivity {

    //Set up all of the editable text boxes.
    TextView examTimeHour;
    TextView examName;
    TextView examTimeMin;
    TextView examYear;
    TextView examMonth;
    TextView examDay;
    TextView examLength;
    TextView codeView;
    User user = null;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference examRef = database.getReference("Proproct/Exams");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);
        Intent intent = getIntent();
        user = intent.getParcelableExtra(createClassActivity.CREATE_EXAM_EXTRA);
        examName = findViewById(R.id.examCreateRoomNameEntry);
        examTimeHour = findViewById(R.id.examCreateRoomTimeHour);
        examTimeMin = findViewById(R.id.examCreateRoomTimeMin);
        examYear = findViewById(R.id.examCreateRoomDayYear);
        examMonth = findViewById(R.id.examCreateRoomDayMonth);
        examDay = findViewById(R.id.examCreateRoomDay);
        codeView = findViewById(R.id.examRoomGeneratedCode);
        examLength = findViewById(R.id.createExamExamLength);
        Calendar calendar = Calendar.getInstance();
        //set each box to the current date and time.
        examYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        examMonth.setText(Integer.toString(calendar.get(Calendar.MONTH) + 1));
        examDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
        examTimeHour.setText(String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        examTimeMin.setText(String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.MINUTE)));
    }
    //TODO associate an exam with a class that already exists through the class ID.

    public boolean legalCreate() {
        return !(examName.getText().toString().trim().equals(""));
    }

    public void createExam() {
        DatabaseReference tempExamRef = examRef;
        DatabaseReference userExamRef = database.getReference("Proproct/Users/" + user.ID + "/exams");
        tempExamRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int prospectiveCode = generateRandomDigits();
                //Handle the (unlikely) case that the same ID is generated.
                while (snapshot.hasChild(String.valueOf(prospectiveCode))) {
                    //This should only happen ~ 1 / 999999999 times, but this handles dupes.
                    prospectiveCode = generateRandomDigits();
                }

                String code = String.valueOf(prospectiveCode);
                examRef.child(code).child("Students").setValue("");
                examRef.child(code).child("Owner").setValue(user.email);
                examRef.child(code).child("Profname").setValue(user.username);
                examRef.child(code).child("TAs").setValue("");
                examRef.child(code).child("Title").setValue(examName.getText().toString().trim());
                examRef.child(code).child("startTimeHour").setValue(examTimeHour.getText().toString());
                examRef.child(code).child("startTimeMin").setValue(examTimeMin.getText().toString());
                examRef.child(code).child("dateYear").setValue(examYear.getText().toString());
                examRef.child(code).child("dateMonth").setValue(examMonth.getText().toString());
                examRef.child(code).child("dateDay").setValue(examDay.getText().toString());
                examRef.child(code).child("length").setValue(examLength.getText().toString());
                userExamRef.child(code).setValue(examName.getText().toString().trim());
                codeView.setText(code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database failure. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //When the professor clicks the create button.
    public void onCreateClick(View view) {
        if (legalCreate()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("Do you wish to create class: '" + examName.getText().toString().trim() + "' ?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //The user has clicked yes.
                            //set the TextView text to the random code.
                            createExam();
                            Toast.makeText(getApplicationContext(), "Exam has been created.", Toast.LENGTH_SHORT).show();
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
}