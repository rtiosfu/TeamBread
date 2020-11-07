package com.example.loginscreen.roomcode.User;

import com.example.loginscreen.roomcode.User.userType;

import java.util.ArrayList;

//This class will be called once a student has logged in, populating the various attributes.

public class Student extends User {
    //Will contain student number as well as location data
    public ArrayList<String> Student_Info;

    public ArrayList<String> enrolledClasses;

    public ArrayList<String> enrolledRooms;

    public Student(String mail, String fName, String lName, userType uType, String idNum){
        email = mail;
//        password = pWord;
        username = fName + lName;
        type = uType;
        ID = idNum;
        currentRoom = "NONE";
        receivesUpdates = false;
        canViewInfo = false;
        receivesUpdates = false;
        canCreateCourses = false;
        canCreateExams = false;

    }

    //add location later
}
