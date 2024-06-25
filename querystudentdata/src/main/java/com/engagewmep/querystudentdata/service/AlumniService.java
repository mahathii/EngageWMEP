package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Alumni;
import com.engagewmep.querystudentdata.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return alumniRepository.findByEmailAddressOrFirstNameOrLastName(term, term, term);
    }

    // Delete an Alumni by id
    public void deleteAlumni(Long id) {
        alumniRepository.deleteById(id);
    }
}
