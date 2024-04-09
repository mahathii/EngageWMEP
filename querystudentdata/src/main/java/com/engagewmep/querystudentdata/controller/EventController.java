package com.engagewmep.querystudentdata.controller;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.Student;
import com.engagewmep.querystudentdata.repository.EventRepository;
import com.engagewmep.querystudentdata.repository.StudentRepository;
import com.engagewmep.querystudentdata.service.EventService;
import com.engagewmep.querystudentdata.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StudentRepository studentRepository;

    private final StudentService studentService;

    @Autowired
    public EventController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{studentId}/events")
    public ResponseEntity<List<Event>> getEventsByStudentId(@PathVariable String studentId) {
        List<Event> events = studentService.getEventsByStudentStudentId(studentId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudentsByMultipleEvents(
            @RequestParam List<Long> eventId,
            @RequestParam(required = false) String strategy // Add this parameter
    ) {
        List<Student> students;
        if (eventId == null || eventId.isEmpty()) {
            students = studentRepository.findAll();
        } else {
            if ("ALL".equalsIgnoreCase(strategy)) {
                students = eventService.getStudentsAttendingAllEvents(eventId);
            } else {
                students = eventService.getStudentsByMultipleEvents(eventId);
            }
        }
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }
}
