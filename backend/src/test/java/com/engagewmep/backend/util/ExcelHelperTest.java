package com.engagewmep.backend.util;

import com.engagewmep.backend.model.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelHelperTest {

    @Test
    public void testGetExcelHeaders() throws Exception {
        // Create an in-memory workbook with a header row
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        String[] expectedHeaders = {"Header1", "Header2", "Header3"};
        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(expectedHeaders[i]);
        }
        // Write workbook to a byte array and create an InputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        List<String> headers = ExcelHelper.getExcelHeaders(bis);
        assertEquals(expectedHeaders.length, headers.size(), "Header count should match");
        for (int i = 0; i < expectedHeaders.length; i++) {
            assertEquals(expectedHeaders[i], headers.get(i), "Header value should match");
        }
    }

    @Test
    public void testParseExcelFile() throws Exception {
        // Create an in-memory workbook with a header row and one student row
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Header row: using expected header names as per ExcelHelper usage
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "student: Student ID",
                "student: Last Name",
                "student: First Name",
                "Student Profile: Degree Level",
                "Student Profile: Graduation Date",
                "Student Profile: Major",
                "Student Profile: College",
                "Student Profile: Admin Major",
                "Student Profile: Email",
                "Student Profile: Ethnicity"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Create a student data row
        Row row1 = sheet.createRow(1);
        String studentId = "S001";
        String lastName = "Doe";
        String firstName = "John";
        String degreeLevel = "Bachelor";
        // For graduation date, we set a date value; here we use 2025-12-31
        LocalDate gradLocalDate = LocalDate.of(2025, 12, 31);
        Date gradDate = Date.valueOf(gradLocalDate);
        String major = "Computer Science";
        String college = "Engineering";
        String adminMajor = "CS";
        String email = "john.doe@example.com";
        String ethnicity = "Hispanic";

        row1.createCell(0).setCellValue(studentId);
        row1.createCell(1).setCellValue(lastName);
        row1.createCell(2).setCellValue(firstName);
        row1.createCell(3).setCellValue(degreeLevel);

        // Create a date cell for graduation date with an appropriate style
        Cell dateCell = row1.createCell(4);
        dateCell.setCellValue(gradDate);
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        short dateFormat = creationHelper.createDataFormat().getFormat("yyyy-MM-dd");
        dateCellStyle.setDataFormat(dateFormat);
        dateCell.setCellStyle(dateCellStyle);

        row1.createCell(5).setCellValue(major);
        row1.createCell(6).setCellValue(college);
        row1.createCell(7).setCellValue(adminMajor);
        row1.createCell(8).setCellValue(email);
        row1.createCell(9).setCellValue(ethnicity);

        // Write workbook to a byte array and create an InputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        List<Student> students = ExcelHelper.parseExcelFile(bis);
        assertEquals(1, students.size(), "One student should be parsed");

        Student student = students.get(0);
        assertEquals(studentId, student.getStudentId(), "Student ID should match");
        assertEquals(lastName, student.getLastName(), "Last name should match");
        assertEquals(firstName, student.getFirstName(), "First name should match");
        assertEquals(degreeLevel, student.getDegreeLevel(), "Degree level should match");
        assertEquals(gradDate, student.getGraduationDate(), "Graduation date should match");
        assertEquals(major, student.getMajor(), "Major should match");
        assertEquals(college, student.getCollege(), "College should match");
        assertEquals(adminMajor, student.getAdminMajor(), "Admin major should match");
        assertEquals(email, student.getEmail(), "Email should match");
        assertEquals(ethnicity, student.getEthnicity(), "Ethnicity should match");
    }
}
