package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {

    @Query("SELECT ea FROM EventAttendance ea WHERE ea.event.eventDate BETWEEN :startDate AND :endDate")
    List<EventAttendance> findByEventDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT ea FROM EventAttendance ea WHERE ea.student.studentId = :studentId")
    List<EventAttendance> findByStudentStudentId(@Param("studentId") String studentId);
    List<EventAttendance> findByEvent(Event event);
    List<EventAttendance> findByStudentId(Long studentId);
    List<EventAttendance> findByEventIdIn(List<Long> eventIds);
    boolean existsByEventAndStudent(Event event, Student student);



}

