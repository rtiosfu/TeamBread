package com.example.loginscreen;

import junit.framework.TestCase;

// Dylan Gilson

public class createClassTest extends TestCase {

    // cannot test canSubmit() due to implementation of function and dependencies

    public void testDoesEmailMatch() {
        boolean output;
        boolean expected = false;

        signUpActivity temp = new signUpActivity();
        output = temp.doesEmailMatch();

        assertEquals(output, expected);
    }
}