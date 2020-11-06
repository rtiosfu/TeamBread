package com.example.loginscreen.roomcode.User;

import com.example.loginscreen.roomcode.User.userType;

import java.util.ArrayList;

//This class will be called once a student has logged in, populating the various attributes.

public class Student extends User {
    public ArrayList<String> Student_Info;

    public ArrayList<String> enrolledClasses;

    public ArrayList<String> enrolledRooms;

    public Student(String mail, String pWord, String uName, userType uType){
        email = mail;
        password = pWord;
        username = uName;
        type = uType;
        currentRoom = "NONE";
        receivesUpdates = false;
        canViewInfo = false;
        receivesUpdates = false;
        canCreateCourses = false;
        canCreateExams = false;

    }

    //add location later
}
