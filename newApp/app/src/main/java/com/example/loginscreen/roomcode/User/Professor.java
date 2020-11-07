package com.example.loginscreen.roomcode.User;

import com.example.loginscreen.roomcode.User.userType;

import java.util.ArrayList;

public class Professor extends User {

    public ArrayList<String> ownedClasses;
    public ArrayList<String> ownedRooms;
    public ArrayList<String> TAs;

    public Professor(String mail, String fName, String lName, userType uType, String idNum){
        email = mail;
        username = fName + lName;
        type = uType;
        ID = idNum;
        currentRoom = "NONE";
        receivesUpdates = true;
        canViewInfo = true;
        receivesUpdates = true;
        canCreateCourses = true;
        canCreateExams = true;
    }
}
