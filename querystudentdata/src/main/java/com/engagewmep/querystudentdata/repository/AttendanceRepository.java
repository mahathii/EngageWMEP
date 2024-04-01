package com.engagewmep.querystudentdata.repository;

import com.engagewmep.querystudentdata.model.Event;
import com.engagewmep.querystudentdata.model.EventAttendance;
import com.engagewmep.querystudentdata.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<EventAttendance, Long> {
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.student.studentId = :studentId")
    List<EventAttendance> findByStudentStudentId(@Param("studentId") String studentId);
    List<EventAttendance> findByEvent(Event event);
    List<EventAttendance> findByStudentId(Long studentId);

    List<EventAttendance> findByEventIdIn(List<Long> eventIds);




}

