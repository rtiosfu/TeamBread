package com.example.loginscreen;

import junit.framework.TestCase;

// Dylan Gilson

public class createClassTest extends TestCase {

    // cannot test generateRandomDigits() due to randomness of output

    public void testLookupCode() {
        int input = 3;
        boolean output;
        boolean expected = false;

        createClass temp = new createClass();
        output = temp.lookupCode(input);

        assertEquals(output, expected);
    }
}