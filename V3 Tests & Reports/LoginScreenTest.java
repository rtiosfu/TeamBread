package com.example.loginscreen;

import junit.framework.TestCase;

// Dylan Gilson

public class LoginScreenTest extends TestCase {

    public void testValidateEmail() {
        String input = "admin@";
        boolean output;
        boolean expected = true;

        LoginScreen loginScreen = new LoginScreen();
        output = loginScreen.validateEmail(input);

        assertEquals(output, expected);
    }

    public void testCountCharOccurrences() {
        String input1 = "admin@";
        String input2 = "@";
        int output;
        int expected = 1;

        LoginScreen loginScreen = new LoginScreen();
        output = loginScreen.countCharOccurrences(input1, input2);

        assertEquals(output, expected);
    }
}