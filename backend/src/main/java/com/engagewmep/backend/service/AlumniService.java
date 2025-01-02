package com.engagewmep.backend.service;

import com.engagewmep.backend.model.Alumni;
import com.engagewmep.backend.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


@Service
public class AlumniService {

    private final AlumniRepository alumniRepository;

    @Autowired
    public AlumniService(AlumniRepository alumniRepository) {
        this.alumniRepository = alumniRepository;
    }

    // Save or update Alumni
    public Alumni saveOrUpdateAlumni(Alumni alumni) {
        return alumniRepository.save(alumni);
    }

    // Retrieve all Alumni entries
    public List<Alumni> findAllAlumni() {
        return alumniRepository.findAll();
    }

    // Get a single Alumni by id
    public Alumni findAlumniById(Long id) {
        return alumniRepository.findById(id).orElse(null);
    }

    public List<Alumni> searchAlumni(String term) {
        return alumniRepository.findByEmailAddress(term);
    }

    // Delete an Alumni by id
    public void deleteAlumni(Long id) {
        alumniRepository.deleteById(id);
    }

    public Map<String, List<String>> getFilterValues() {
        List<Alumni> alumni = alumniRepository.findAll();

        Map<String, List<String>> filterValues = new HashMap<>();
        filterValues.put("raceEthnicity", alumni.stream().map(Alumni::getRaceEthnicity).distinct().collect(Collectors.toList()));
        filterValues.put("gender", alumni.stream().map(Alumni::getGender).distinct().collect(Collectors.toList()));
        filterValues.put("ncsuGraduate", alumni.stream().map(Alumni::getNcsuGraduate).distinct().collect(Collectors.toList()));
        filterValues.put("collegeOfEngineeringGraduate", alumni.stream().map(Alumni::getCollegeOfEngineeringGraduate).distinct().collect(Collectors.toList()));
        filterValues.put("yearOfGraduation", alumni.stream().map(Alumni::getYearOfGraduation).distinct().collect(Collectors.toList()));
        filterValues.put("stpParticipationAndYear", alumni.stream().map(Alumni::getStpParticipationAndYear).distinct().collect(Collectors.toList()));
        filterValues.put("major", alumni.stream().map(Alumni::getMajor).distinct().collect(Collectors.toList()));
        filterValues.put("currentCity", alumni.stream().map(Alumni::getCurrentCity).distinct().collect(Collectors.toList()));
        filterValues.put("currentState", alumni.stream().map(Alumni::getCurrentState).distinct().collect(Collectors.toList()));
        filterValues.put("currentZipCode", alumni.stream().map(Alumni::getCurrentZipCode).distinct().collect(Collectors.toList()));
        filterValues.put("mentoringOptIn", alumni.stream().map(Alumni::getMentoringOptIn).distinct().collect(Collectors.toList()));

        return filterValues;
    }
}
