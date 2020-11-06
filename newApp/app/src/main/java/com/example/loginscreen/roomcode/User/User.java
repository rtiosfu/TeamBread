package com.example.loginscreen.roomcode.User;

public abstract class User {

    public String email;
    public String password;
    public String username;
    public userType type;
    public String currentRoom;
    public boolean receivesUpdates;
    public boolean canViewInfo;
    public boolean canCreateCourses;
    public boolean canCreateExams;

}

enum userType{
    STUDENT,
    PROFESSOR,
    TA
}

