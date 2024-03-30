package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.AttendanceRepository;
import com.engagewmep.querystudentdata.repository.EventRepository;
import com.engagewmep.querystudentdata.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Event> getEventsByStudentStudentId(String studentId) {
        List<EventAttendance> attendances = attendanceRepository.findByStudentStudentId(studentId);
        return attendances.stream().map(EventAttendance::getEvent).collect(Collectors.toList());
    }
}
