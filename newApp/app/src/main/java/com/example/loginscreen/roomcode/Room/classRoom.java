package com.example.loginscreen.roomcode.Room;

import java.util.ArrayList;

public class classRoom extends Room {
    public String className;

    public classRoom(String rCode, String cName, ArrayList<String> Students){
        roomCode = rCode;
        className = cName;
        registeredStudents = Students;
    }
}
