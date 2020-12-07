package com.example.loginscreen.roomcode.Room;

import junit.framework.TestCase;

public class RoomTest extends TestCase {
    public void testDescribeContents() {
        int output;
        int expected = 0;

        Room temp = new Room();
        output = temp.describeContents();

        assertEquals(output, expected);
    }
}
