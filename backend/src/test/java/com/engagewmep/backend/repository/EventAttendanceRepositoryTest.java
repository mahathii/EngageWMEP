package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EventAttendanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventAttendanceRepository eventAttendanceRepository;

    @Test
    public void testFindByEventDateBetween() {
        // Create two events with different dates
        Event event1 = new Event();
        event1.setEventDate(LocalDate.of(2025, 1, 10));
        entityManager.persist(event1);

        Event event2 = new Event();
        event2.setEventDate(LocalDate.of(2025, 2, 15));
        entityManager.persist(event2);

        // Create a student for both attendances
        Student student = new Student();
        student.setStudentId("S001");
        entityManager.persist(student);

        // Create EventAttendance records
        EventAttendance ea1 = new EventAttendance();
        ea1.setEvent(event1);
        ea1.setStudent(student);
        entityManager.persist(ea1);

        EventAttendance ea2 = new EventAttendance();
        ea2.setEvent(event2);
        ea2.setStudent(student);
        entityManager.persist(ea2);

        entityManager.flush();

        // Query for events between Jan 1 and Jan 31, 2025 - should return only event1's attendance
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);
        List<EventAttendance> results = eventAttendanceRepository.findByEventDateBetween(startDate, endDate);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEvent().getEventDate()).isEqualTo(event1.getEventDate());
    }

    @Test
    public void testFindByStudentStudentId() {
        // Create a student with a given studentId
        Student student = new Student();
        student.setStudentId("S002");
        entityManager.persist(student);

        // Create an event
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 3, 5));
        entityManager.persist(event);

        // Create an attendance record linking the student to the event
        EventAttendance attendance = new EventAttendance();
        attendance.setEvent(event);
        attendance.setStudent(student);
        entityManager.persist(attendance);

        entityManager.flush();

        List<EventAttendance> results = eventAttendanceRepository.findByStudentStudentId("S002");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStudent().getStudentId()).isEqualTo("S002");
    }

    @Test
    public void testFindByEvent() {
        // Create an event
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 4, 20));
        entityManager.persist(event);

        // Create two different students and link them with the same event
        Student student1 = new Student();
        student1.setStudentId("S003");
        entityManager.persist(student1);

        Student student2 = new Student();
        student2.setStudentId("S004");
        entityManager.persist(student2);

        EventAttendance ea1 = new EventAttendance();
        ea1.setEvent(event);
        ea1.setStudent(student1);
        entityManager.persist(ea1);

        EventAttendance ea2 = new EventAttendance();
        ea2.setEvent(event);
        ea2.setStudent(student2);
        entityManager.persist(ea2);

        entityManager.flush();

        List<EventAttendance> results = eventAttendanceRepository.findByEvent(event);
        assertThat(results).hasSize(2);
    }

    @Test
    public void testFindByStudentId() {
        // Create a student and persist it so that it gets an ID
        Student student = new Student();
        student.setStudentId("S005");
        student = entityManager.persistAndFlush(student);

        // Create an event
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 5, 15));
        entityManager.persist(event);

        // Create an attendance record
        EventAttendance attendance = new EventAttendance();
        attendance.setEvent(event);
        attendance.setStudent(student);
        entityManager.persist(attendance);
        entityManager.flush();

        List<EventAttendance> results = eventAttendanceRepository.findByStudentId(student.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStudent().getId()).isEqualTo(student.getId());
    }

    @Test
    public void testFindByEventIdIn() {
        // Create two events
        Event event1 = new Event();
        event1.setEventDate(LocalDate.of(2025, 6, 10));
        event1 = entityManager.persistAndFlush(event1);

        Event event2 = new Event();
        event2.setEventDate(LocalDate.of(2025, 6, 20));
        event2 = entityManager.persistAndFlush(event2);

        // Create a student
        Student student = new Student();
        student.setStudentId("S006");
        entityManager.persist(student);

        // Create attendance records for both events
        EventAttendance ea1 = new EventAttendance();
        ea1.setEvent(event1);
        ea1.setStudent(student);
        entityManager.persist(ea1);

        EventAttendance ea2 = new EventAttendance();
        ea2.setEvent(event2);
        ea2.setStudent(student);
        entityManager.persist(ea2);

        entityManager.flush();

        // Query using only one event id in the list
        List<EventAttendance> results = eventAttendanceRepository.findByEventIdIn(Arrays.asList(event1.getId()));
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEvent().getId()).isEqualTo(event1.getId());
    }

    @Test
    public void testExistsByEventAndStudent() {
        // Create event and student
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 7, 15));
        entityManager.persist(event);

        Student student = new Student();
        student.setStudentId("S007");
        entityManager.persist(student);

        // Create an attendance record
        EventAttendance attendance = new EventAttendance();
        attendance.setEvent(event);
        attendance.setStudent(student);
        entityManager.persist(attendance);

        entityManager.flush();

        boolean exists = eventAttendanceRepository.existsByEventAndStudent(event, student);
        assertThat(exists).isTrue();

        // Test for non-existent relationship
        Student studentNotAttended = new Student();
        studentNotAttended.setStudentId("S008");
        entityManager.persist(studentNotAttended);
        entityManager.flush();

        boolean existsFalse = eventAttendanceRepository.existsByEventAndStudent(event, studentNotAttended);
        assertThat(existsFalse).isFalse();
    }

    @Test
    public void testFindByEventAndStudent() {
        // Create event and student
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 8, 1));
        entityManager.persist(event);

        Student student = new Student();
        student.setStudentId("S009");
        entityManager.persist(student);

        // Create an attendance record
        EventAttendance attendance = new EventAttendance();
        attendance.setEvent(event);
        attendance.setStudent(student);
        entityManager.persist(attendance);

        entityManager.flush();

        Optional<EventAttendance> result = eventAttendanceRepository.findByEventAndStudent(event, student);
        assertThat(result).isPresent();
        assertThat(result.get().getEvent().getId()).isEqualTo(event.getId());
        assertThat(result.get().getStudent().getStudentId()).isEqualTo(student.getStudentId());
    }
}
