package com.engagewmep.backend.util;

import com.engagewmep.backend.model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    public static List<String> getExcelHeaders(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream); // Create a Workbook for .xlsx files
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
        Row headerRow = sheet.getRow(0); // Assume the first row contains headers

        List<String> headers = new ArrayList<>();
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim()); // Add header values to the list
            }
        }

        workbook.close(); // Close the workbook to free resources
        return headers;
    }

    public static List<Student> parseExcelFile(InputStream is) throws Exception {
        List<Student> students = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Student student = new Student();
            student.setStudentId(getCellValueAsString(row.getCell(0)));
            student.setLastName(getCellValueAsString(row.getCell(1)));
            student.setFirstName(getCellValueAsString(row.getCell(2)));
            student.setDegreeLevel(getCellValueAsString(row.getCell(3)));
            student.setGraduationDate(getCellValueAsDate(row.getCell(4)));
            student.setMajor(getCellValueAsString(row.getCell(5)));
            student.setCollege(getCellValueAsString(row.getCell(6)));
            student.setAdminMajor(getCellValueAsString(row.getCell(7)));
            student.setEmail(getCellValueAsString(row.getCell(8)));
            student.setEthnicity(getCellValueAsString(row.getCell(9)));
            students.add(student);
        }

        workbook.close();
        return students;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue());
                } else {
                    double numericValue = cell.getNumericCellValue();
                    long longValue = (long) numericValue;
                    return String.valueOf(longValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    private static Date getCellValueAsDate(Cell cell) {
        if (cell != null && DateUtil.isCellDateFormatted(cell)) {
            return new Date(cell.getDateCellValue().getTime());
        }
        return null;
    }

}

