package com.engagewmep.backend.controller;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import com.engagewmep.backend.repository.EventRepository;
import com.engagewmep.backend.service.EventAttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@RestController
@RequestMapping("/api/event-attendance")
public class EventAttendanceController {

    @Autowired
    private EventAttendanceService eventAttendanceService;

    @Autowired
    private EventAttendanceRepository eventAttendanceRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/{eventId}/upload")
    public ResponseEntity<String> uploadAttendance(@PathVariable("eventId") Long eventId,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            eventAttendanceService.processAttendanceFile(eventId, file);
            return ResponseEntity.ok("Attendance uploaded successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload attendance: " + e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}/delete")
    public ResponseEntity<String> deleteAllAttendance(@PathVariable("eventId") Long eventId) {
        try {
            eventAttendanceService.deleteAttendance(eventId);
            return ResponseEntity.ok("All attendance records deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete attendance: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventAttendance(@PathVariable Long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            List<EventAttendance> attendanceRecords = eventAttendanceRepository.findByEvent(event);

            if (attendanceRecords.isEmpty()) {
                return ResponseEntity.ok("No attendance uploaded yet.");
            }

            return ResponseEntity.ok(attendanceRecords);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}


