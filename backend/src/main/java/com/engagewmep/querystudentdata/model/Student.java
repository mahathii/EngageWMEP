package com.engagewmep.querystudentdata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String lastName;
    private String firstName;
    private String degreeLevel;
    private Date graduationDate;
    private String major;
    private String college;
    private String adminMajor;
    private String email;
    private String ethnicity;

    public void filterColumns(List<String> selectedColumns) {
        Map<String, Object> filteredData = new HashMap<>();

        // Populate filtered data map based on selected columns
        for (String columnName : selectedColumns) {
            switch (columnName) {
                case "studentId":
                    filteredData.put("studentId", this.studentId);
                    break;
                case "lastName":
                    filteredData.put("lastName", this.lastName);
                    break;
                case "firstName":
                    filteredData.put("firstName", this.firstName);
                    break;
                case "degreeLevel":
                    filteredData.put("degreeLevel", this.degreeLevel);
                    break;
                case "graduationDate":
                    filteredData.put("graduationDate", this.graduationDate);
                    break;
                case "major":
                    filteredData.put("major", this.major);
                    break;
                case "college":
                    filteredData.put("college", this.college);
                    break;
                case "adminMajor":
                    filteredData.put("adminMajor", this.adminMajor);
                    break;
                case "email":
                    filteredData.put("email", this.email);
                    break;
                case "ethnicity":
                    filteredData.put("ethnicity", this.ethnicity);
                    break;
            }
        }

        // Clear existing data and set filtered data
        this.clearData();
        this.setData(filteredData);
    }

    private void setData(Map<String, Object> data) {
        this.studentId = (String) data.get("studentId");
        this.lastName = (String) data.get("lastName");
        this.firstName = (String) data.get("firstName");
        this.degreeLevel = (String) data.get("degreeLevel");
        this.graduationDate = (Date) data.get("graduationDate");
        this.major = (String) data.get("major");
        this.college = (String) data.get("college");
        this.adminMajor = (String) data.get("adminMajor");
        this.email = (String) data.get("email");
        this.ethnicity = (String) data.get("ethnicity");
    }

    private void clearData() {
        this.studentId = null;
        this.lastName = null;
        this.firstName = null;
        this.degreeLevel = null;
        this.graduationDate = null;
        this.major = null;
        this.college = null;
        this.adminMajor = null;
        this.email = null;
        this.ethnicity = null;
    }
}
