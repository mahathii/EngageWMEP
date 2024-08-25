package com.engagewmep.querystudentdata.repository;

import com.engagewmep.querystudentdata.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
