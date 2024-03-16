package com.engagewmep.querystudentdata.repository;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<EventAttendance, Long> {
    List<EventAttendance> findByEvent(Event event);
}
