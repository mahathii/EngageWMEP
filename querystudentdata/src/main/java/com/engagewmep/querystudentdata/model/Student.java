package com.engagewmep.querystudentdata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.Set;

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
}