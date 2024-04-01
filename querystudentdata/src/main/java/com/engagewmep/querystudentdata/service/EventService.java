package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.AttendanceRepository;
import com.engagewmep.querystudentdata.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public List<Student> getStudentsByMultipleEvents(List<Long> eventIds) {
        // This assumes you have or will add the findByEventIdIn method to the repository
        List<EventAttendance> attendances = attendanceRepository.findByEventIdIn(eventIds);

        // Use a Set to avoid duplicate students if they attended multiple events
        Set<Student> uniqueStudents = new HashSet<>();
        for (EventAttendance attendance : attendances) {
            uniqueStudents.add(attendance.getStudent());
        }

        // Convert the Set to a List to return
        return new ArrayList<>(uniqueStudents);
    }
}
