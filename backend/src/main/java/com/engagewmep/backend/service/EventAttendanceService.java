package com.engagewmep.backend.service;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import com.engagewmep.backend.repository.EventRepository;
import com.engagewmep.backend.repository.StudentRepository;
import com.engagewmep.backend.util.ExcelHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class EventAttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EventAttendanceRepository eventAttendanceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void processAttendanceFile(Long eventId, MultipartFile file) throws Exception {
        try {

            validateExcelSheet(file);
            InputStream inputStream = file.getInputStream();
            List<Student> studentsFromExcel = ExcelHelper.parseExcelFile(inputStream);

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            for (Student student : studentsFromExcel) {
                System.out.println("Processing Student ID: " + student.getStudentId());

                // Check if student ID is null
                if (student.getStudentId() != null) {
                    // Check if student already exists by student ID
                    Optional<Student> existingStudentOpt = studentRepository.findByStudentId(student.getStudentId());

                    Student existingStudent;
                    if (existingStudentOpt.isPresent()) {
                        // Use the existing student
                        existingStudent = existingStudentOpt.get();
                        System.out.println("Existing student found: " + existingStudent.getStudentId());
                    } else {
                        // Save the new student if not found in the database
                        existingStudent = studentRepository.save(student);
                        System.out.println("New student added: " + existingStudent.getStudentId());
                    }

                    if (eventAttendanceRepository.existsByEventAndStudent(event, existingStudent)) {
                        throw new DataIntegrityViolationException("Attendance already exists for this event and student.");
                    }
                    // Check if the attendance record already exists for this event and student
                    if (!eventAttendanceRepository.existsByEventAndStudent(event, existingStudent)) {
                        EventAttendance attendance = new EventAttendance();
                        attendance.setEvent(event);
                        attendance.setStudent(existingStudent);

                        // Save attendance record
                        eventAttendanceRepository.save(attendance);
                    } else {
                        System.out.println("Attendance already exists for student: " + existingStudent.getStudentId());
                    }
                } else {
                    System.out.println("Skipping student with null ID");
                }
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation: " + e.getMessage());
            throw new RuntimeException("Duplicate entry detected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
            throw new RuntimeException("Failed to process file: " + e.getMessage());
        }
    }

    public void validateExcelSheet(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();

        List<String> headers = ExcelHelper.getExcelHeaders(inputStream);
        headers = headers.stream()
                .map(String::trim)  // Trim whitespace from each header
                .filter(h -> !h.isEmpty())  // Remove empty headers
                .toList();

        List<String> expectedHeaders = List.of(
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
        );
        System.out.println("Extracted Headers: " + headers);
        System.out.println("Expected Headers: " + expectedHeaders);

        if (!headers.equals(expectedHeaders)) {
            throw new RuntimeException("Incorrect Excel sheet format. Expected headers: " + expectedHeaders);
        }
    }

    @Transactional
    public void deleteAttendance(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        EventAttendance attendance = eventAttendanceRepository.findByEventAndStudent(event, student)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        eventAttendanceRepository.delete(attendance);
    }

}
