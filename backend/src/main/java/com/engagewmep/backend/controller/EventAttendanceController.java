package com.engagewmep.backend.controller;

import com.engagewmep.backend.service.EventAttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/event-attendance")
public class EventAttendanceController {

    @Autowired
    private EventAttendanceService eventAttendanceService;

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
}


