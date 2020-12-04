////////////////////////////////////////////
//TEAM BREAD
//Room.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode.Room;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//Unsure if this Room class will end up being used. Will likely call data from the database instead.
public class Room implements Parcelable {
    public String roomName;
    public String roomCode;
//    public ArrayList<String> registeredStudents;

    public Room(String name, String code){
        roomName = name;
        roomCode = code;
    }

    public Room(Parcel in){
        roomName = in.readString();
        roomCode = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public Room() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomName);
        dest.writeString(roomCode);
    }
}
