package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long>, JpaSpecificationExecutor<Alumni> {

    // Custom query for searching by email address
    List<Alumni> findByEmailAddress(String emailAddress);

}
