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

    public List<Student> getStudentsByMultipleEvents(List<Long> eventIds) {

        List<EventAttendance> attendances = attendanceRepository.findByEventIdIn(eventIds);

        Set<Student> uniqueStudents = new HashSet<>();
        for (EventAttendance attendance : attendances) {
            uniqueStudents.add(attendance.getStudent());
        }

        return new ArrayList<>(uniqueStudents);
    }

    public List<Student> getStudentsAttendingAllEvents(List<Long> eventIds) {
        List<Student> studentsAttendingAllEvents;

        if (eventIds.isEmpty()) {
            // If no events are selected, return an empty list
            studentsAttendingAllEvents = List.of();
        } else {
            // Fetch attendance records for all selected events
            List<EventAttendance> allAttendances = attendanceRepository.findByEventIdIn(eventIds);

            // Group attendance records by student
            var attendanceByStudent = allAttendances.stream()
                    .collect(Collectors.groupingBy(EventAttendance::getStudent));

            // Filter students who attended all events
            studentsAttendingAllEvents = attendanceByStudent.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == eventIds.size())
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
        }

        return studentsAttendingAllEvents;
    }
}
