package com.example.loginscreen;

import junit.framework.TestCase;

// Dylan Gilson

public class enterRoomCodeTest extends TestCase {

    public void testLookupCode() {
        String input = "3";
        int output;
        int expected = -1;

        enterRoomCode temp = new enterRoomCode();
        output = temp.lookupCode(input);

        assertEquals(output, expected);
    }
}