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
import java.util.Optional;

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

    @GetMapping("/{eventId}/students")
    public ResponseEntity<List<Student>> getStudentsByEvent(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            List<Student> students = eventService.getStudentsByEvent(event);
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // New endpoint to handle multiple event IDs

    @GetMapping("/student/{studentId}/events")
    public ResponseEntity<List<Event>> getEventsByStudentId(@PathVariable String studentId) {
        List<Event> events = studentService.getEventsByStudentStudentId(studentId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudentsByMultipleEvents(@RequestParam List<Long> eventId) {
        List<Student> students = eventService.getStudentsByMultipleEvents(eventId);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }
}
