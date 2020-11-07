package utils;

public class ExcelUtilsTest {

	public static void main(String[] args) {
		String path = "./data/TestData.xlsx";
		String sheet = "Sheet1";
		ExcelUtils test = new ExcelUtils(path, sheet);
		
		test.getStudents();
	}
}
