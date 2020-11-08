package com.example.loginscreen.roomcode.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {

    public String email;
    public String username;
    public String ID;
    public userType type;
    public String currentRoom;
    public boolean receivesUpdates;
    //can they view exam info
    public boolean canViewInfo;
    public boolean canCreateCourses;
    public boolean canCreateExams;
    public String loc;
    //For Professors, this will mean classes and exams that they own
    //For TAs or Students, this will mean classes and exams that they are registered for.
    public ArrayList<String> classes;
    public ArrayList<String> exams;

    public User(){

    }

    public User(String mail, String fName, String lName, userType uType, String idNum){
        email = mail;
        username = fName + lName;
        type = uType;
        ID = idNum;
        currentRoom = "NONE";
        receivesUpdates = false;
        canViewInfo = false;
        receivesUpdates = false;
        canCreateCourses = false;
        canCreateExams = false;
        classes = null;
        exams = null;

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcelHelper(Parcel out, int flags, ArrayList<String> arr){
        if(arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                out.writeString(arr.get(i));
            }
        }
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(email);
        out.writeString(username);
        out.writeString(ID);
        out.writeInt(type.ordinal());
        out.writeString(currentRoom);
        out.writeInt(receivesUpdates ? 1 : 0);
        out.writeInt(canViewInfo ? 1 : 0);
        out.writeInt(canCreateCourses ? 1 : 0);
        out.writeInt(canCreateExams ? 1 : 0);
        writeToParcelHelper(out, flags, classes);
        writeToParcelHelper(out, flags, exams);
        out.writeString(loc);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    void readParcelHelper(Parcel in, ArrayList<String> arr){
        if(arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                arr.add(in.readString());
            }
        }else{
            arr = null;
        }
    }
    private User(Parcel in) {

        email = in.readString();
        username = in.readString();
        ID = in.readString();
        type = userType.values()[in.readInt()];
        currentRoom = in.readString();
        receivesUpdates = in.readInt() == 1;
        canViewInfo = in.readInt() == 1;
        canCreateCourses = in.readInt() == 1;
        canCreateExams = in.readInt() == 1;
        if(classes != null){
            classes.clear();
            readParcelHelper(in, classes);
        }
        if(exams != null){
            exams.clear();
            readParcelHelper(in, exams);
        }
        loc = in.readString();

    }

}

