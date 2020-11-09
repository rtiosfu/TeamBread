////////////////////////////////////////////
//TEAM BREAD
//Professor.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode.User;

import com.example.loginscreen.roomcode.User.userType;

import java.util.ArrayList;

//Likely deprecated Professor class.
//ownedClasses and ownedRooms have been rolled into the base User class,
//and TAs are now part of classes rather than the Professor class.
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
