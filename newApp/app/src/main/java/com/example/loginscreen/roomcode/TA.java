package com.example.loginscreen.roomcode;

import java.util.ArrayList;

public class TA extends User {

    public ArrayList<String> classes;

    public TA(String mail, String pWord, String uName, userType uType){
        email = mail;
        password = pWord;
        username = uName;
        type = uType;
        currentRoom = "NONE";
        receivesUpdates = true;
        canViewInfo = true;
        receivesUpdates = true;
        canCreateCourses = true;
        canCreateExams = false;
    }
}
