package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.EventAttendanceRepository;
import com.engagewmep.querystudentdata.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EventAttendanceRepository eventAttendanceRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, EventAttendanceRepository eventAttendanceRepository) {
        this.studentRepository = studentRepository;
        this.eventAttendanceRepository = eventAttendanceRepository;
    }

    public List<Event> getEventsByStudentStudentId(String studentId) {
        List<EventAttendance> attendances = eventAttendanceRepository.findByStudentStudentId(studentId);
        return attendances.stream().map(EventAttendance::getEvent).collect(Collectors.toList());
    }

    public List<Student> getStudentsByTimeFrame(LocalDate startDate, LocalDate endDate) {
        return eventAttendanceRepository.findByEventDateBetween(startDate, endDate)
                .stream()
                .map(EventAttendance::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }


}
