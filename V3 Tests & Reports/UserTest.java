package com.example.loginscreen.roomcode.User;

import junit.framework.TestCase;

public class UserTest extends TestCase {
    public void testDescribeContents() {
        int output;
        int expected = 0;

        User temp = new User();
        output = temp.describeContents();

        assertEquals(output, expected);
    }
}
