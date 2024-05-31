package com.engagewmep.querystudentdata.repository;

import com.engagewmep.querystudentdata.model.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {
    // Additional custom methods can be defined here if needed
}