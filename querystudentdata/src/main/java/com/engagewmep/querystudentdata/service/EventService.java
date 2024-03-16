package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.AttendanceRepository;
import com.engagewmep.querystudentdata.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    // Get students attending a specific event
    public List<Student> getStudentsByEvent(Event event) {
        List<EventAttendance> attendanceList = attendanceRepository.findByEvent(event);
        return attendanceList.stream().map(EventAttendance::getStudent).collect(Collectors.toList());
    }
}
