package com.example.loginscreen.roomcode.Room;

import java.util.ArrayList;

public class examRoom extends Room {
    public String examName;
    public int startTime;
    public int endTime;

    public examRoom(String rCode, String eName, int sTime, int eTime, ArrayList<String> Students){
        roomCode = rCode;
        examName = eName;
        startTime = sTime;
        endTime = eTime;
        registeredStudents = Students;
    }

}
