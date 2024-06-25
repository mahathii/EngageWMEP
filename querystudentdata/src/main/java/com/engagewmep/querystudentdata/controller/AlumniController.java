package com.engagewmep.querystudentdata.controller;

import com.engagewmep.querystudentdata.model.Alumni;
import com.engagewmep.querystudentdata.service.AlumniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
public class AlumniController {

    private final AlumniService alumniService;

    @Autowired
    public AlumniController(AlumniService alumniService) {
        this.alumniService = alumniService;
    }

    @PostMapping
    public ResponseEntity<Alumni> addAlumni(@RequestBody Alumni alumni) {
        Alumni savedAlumni = alumniService.saveOrUpdateAlumni(alumni);
        return ResponseEntity.ok(savedAlumni);
    }

    @GetMapping
    public ResponseEntity<List<Alumni>> getAllAlumni() {
        List<Alumni> alumni = alumniService.findAllAlumni();
        return ResponseEntity.ok(alumni);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumni> getAlumniById(@PathVariable Long id) {
        Alumni alumni = alumniService.findAlumniById(id);
        return alumni != null ? ResponseEntity.ok(alumni) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumni> updateAlumni(@PathVariable Long id, @RequestBody Alumni alumniDetails) {
        Alumni existingAlumni = alumniService.findAlumniById(id);
        if (existingAlumni == null) {
            return ResponseEntity.notFound().build();
        }
        // Assuming setter methods are present in Alumni model
        existingAlumni.setFirstName(alumniDetails.getFirstName());
        existingAlumni.setLastName(alumniDetails.getLastName());
        // Add other fields similarly
        alumniService.saveOrUpdateAlumni(existingAlumni);
        return ResponseEntity.ok(existingAlumni);
    }

    @GetMapping("/search")
    public ResponseEntity<Alumni> searchAlumni(@RequestParam String term) {
        List<Alumni> alumniList = alumniService.searchAlumni(term);
        return ResponseEntity.ok(alumniList.isEmpty() ? null : alumniList.get(0));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlumni(@PathVariable Long id) {
        if (alumniService.findAlumniById(id) != null) {
            alumniService.deleteAlumni(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
