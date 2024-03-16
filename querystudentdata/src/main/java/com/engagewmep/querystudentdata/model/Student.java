package com.engagewmep.querystudentdata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentID;
    private String lastName;
    private String firstName;
    private String degreeLevel;
    private LocalDate graduationDate;
    private String major;
    private String college;
    private String adminMajor;
    // Other student fields

    // Getters and setters
}