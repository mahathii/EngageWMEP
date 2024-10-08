package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.EventAttendanceRepository;
import com.engagewmep.querystudentdata.repository.EventRepository;
import com.engagewmep.querystudentdata.repository.StudentRepository;
import com.engagewmep.querystudentdata.util.ExcelHelper;
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
}
