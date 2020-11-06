package com.example.loginscreen.roomcode;

import java.util.ArrayList;

public class Professor extends User{

    public ArrayList<String> ownedClasses;
    public ArrayList<String> ownedRooms;
    public ArrayList<String> TAs;

    public Professor(String mail, String pWord, String uName, userType uType){
        email = mail;
        password = pWord;
        username = uName;
        type = uType;
        currentRoom = "NONE";
        receivesUpdates = true;
        canViewInfo = true;
        receivesUpdates = true;
        canCreateCourses = true;
        canCreateExams = true;
    }
}
