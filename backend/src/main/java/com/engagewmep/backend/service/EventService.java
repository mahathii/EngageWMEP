package com.engagewmep.backend.service;

import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import com.engagewmep.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventAttendanceRepository eventAttendanceRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Student> getStudentsByMultipleEvents(List<Long> eventIds) {

        List<EventAttendance> attendances = eventAttendanceRepository.findByEventIdIn(eventIds);

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
            List<EventAttendance> allAttendances = eventAttendanceRepository.findByEventIdIn(eventIds);

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

    public List<Event> getPastEvents() {
        List<Event> allEvents = eventRepository.findAll();
        return allEvents.stream()
                .filter(event -> event.getEventDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }
}