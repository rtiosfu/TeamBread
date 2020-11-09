////////////////////////////////////////////
//TEAM BREAD
//Room.java
//PROGRAMMERS:Ryan
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode.Room;

import java.util.ArrayList;

//Unsure if this Room class will end up being used. Will likely call data from the database instead.
public abstract class Room {
    public String roomName;
    public String roomCode;
    public ArrayList<String> registeredStudents;
}
