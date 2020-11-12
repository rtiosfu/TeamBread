////////////////////////////////////////////
//TEAM BREAD
//ExcelUtils.java
//PROGRAMMERS:Daniel
//KNOWN BUGS: None yet.
//V2 CHANGES: None yet.
////////////////////////////////////////////

package com.example.loginscreen.roomcode.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;

    public ExcelUtils(String excelPath, String sheetName){ //excelPath is the location of the excel file
        try {
            workbook = new XSSFWorkbook(excelPath);
            sheet = workbook.getSheet(sheetName);
        }catch(Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    public static int getRowCount() {
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println("Number of rows:" + rowCount);
        return rowCount;
    }


    public static void getCellData(int rowNum) {
//        try {
//            String student_name = sheet.getRow(rowNum).getCell(0).getStringCellValue();
//            String student_email = sheet.getRow(rowNum).getCell(2).getStringCellValue();
//
//            DataFormatter formatter = new DataFormatter();
//            Object student_number = formatter.formatCellValue(sheet.getRow(rowNum).getCell(1));
//            //double student_number = sheet.getRow(1).getCell(1).getNumericCellValue()
//            System.out.println("Name: " + student_name + ", Student #: " + student_number + ", Email: " + student_email);;
//        }catch(Exception exp) {
//            System.out.println(exp.getCause());
//            System.out.println(exp.getMessage());
//            exp.printStackTrace();
//        }
    }
    public static void getStudents() {
        try {
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i<rowCount; i++) {
                getCellData(i);
            }
        }catch(Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }


}

