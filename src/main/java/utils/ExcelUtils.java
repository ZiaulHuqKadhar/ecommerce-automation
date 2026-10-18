package utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Reads an Excel sheet into Object[][] so it can feed a TestNG @DataProvider. */
public class ExcelUtils {

    public static Object[][] getSheetData(String filePath, String sheetName) {
        DataFormatter formatter = new DataFormatter();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            int rowCount = sheet.getLastRowNum();            // header is row 0
            int colCount = sheet.getRow(0).getLastCellNum();

            Object[][] data = new Object[rowCount][colCount];
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    data[i - 1][j] = formatter.formatCellValue(
                            row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                }
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Cannot read Excel file: " + filePath, e);
        }
    }
}
