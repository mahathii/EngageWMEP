package com.engagewmep.backend.controller;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.repository.EventRepository;
import com.engagewmep.backend.repository.StudentRepository;
import com.engagewmep.backend.service.EventService;
import com.engagewmep.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;


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

    @GetMapping("/columns")
    public ResponseEntity<List<String>> getStudentColumnNames() {
        // Use reflection to get fields from the Student class
        Field[] fields = Student.class.getDeclaredFields();
        List<String> columnNames = Arrays.stream(fields)
                .map(Field::getName)
                .filter(name -> !name.equals("id"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(columnNames);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudentsByMultipleEvents(
            @RequestParam List<Long> eventId,
            @RequestParam(required = false) String strategy,
            @RequestParam(required = false) List<String> columns
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

        if (columns != null && !columns.isEmpty()) {
            for (Student student : students) {
                student.filterColumns(columns);
            }
        }

        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/timeframe")
    public ResponseEntity<List<Student>> getStudentsByTimeFrame(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Student> students = studentService.getStudentsByTimeFrame(startDate, endDate);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody Event newEvent) {
        Event createdEvent = eventRepository.save(newEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setName(updatedEvent.getName());
                    event.setEventDate(updatedEvent.getEventDate());
                    event.setEventTime(updatedEvent.getEventTime());
                    event.setEventLocation(updatedEvent.getEventLocation());
                    Event savedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(savedEvent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok().<Void>build(); // Correct return type
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

