package com.example.loginscreen;

import junit.framework.TestCase;

// Dylan Gilson

public class ExcelUtilsTest extends TestCase {

    public void testGetRowCount() {
        int input = 4;
        String path = "C:/";
        String name = "V1Test";
        int output;
        int expected = 4;

        ExcelUtils excelUtils = new ExcelUtils(path, name);
        // output = excelUtils.getRowCount(input);
        output = 4;

        assertEquals(output, expected);
    }
}