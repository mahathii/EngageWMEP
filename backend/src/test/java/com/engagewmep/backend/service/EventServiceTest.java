package com.engagewmep.backend.service;

import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventAttendanceRepository eventAttendanceRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testGetStudentsByMultipleEvents_EmptyList() {
        List<Long> eventIds = List.of(1L, 2L);
        when(eventAttendanceRepository.findByEventIdIn(eventIds)).thenReturn(Collections.emptyList());

        List<Student> students = eventService.getStudentsByMultipleEvents(eventIds);
        assertTrue(students.isEmpty(), "Expected empty list when no attendance records exist");
    }

    @Test
    public void testGetStudentsByMultipleEvents_UniqueStudents() {
        List<Long> eventIds = List.of(1L, 2L);

        // Create dummy students
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        Student student3 = new Student();
        student3.setId(3L);

        // Create attendances; include duplicate attendance for student1
        EventAttendance attendance1 = new EventAttendance();
        attendance1.setStudent(student1);
        EventAttendance attendance2 = new EventAttendance();
        attendance2.setStudent(student1);
        EventAttendance attendance3 = new EventAttendance();
        attendance3.setStudent(student2);
        EventAttendance attendance4 = new EventAttendance();
        attendance4.setStudent(student3);

        List<EventAttendance> attendances = List.of(attendance1, attendance2, attendance3, attendance4);
        when(eventAttendanceRepository.findByEventIdIn(eventIds)).thenReturn(attendances);

        List<Student> students = eventService.getStudentsByMultipleEvents(eventIds);

        // Verify that the returned list contains unique students
        assertEquals(3, students.size(), "Expected three unique students");
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
        assertTrue(students.contains(student3));
    }

    @Test
    public void testGetStudentsAttendingAllEvents_EmptyEventIds() {
        List<Long> eventIds = Collections.emptyList();
        List<Student> students = eventService.getStudentsAttendingAllEvents(eventIds);
        assertTrue(students.isEmpty(), "Expected empty list when no event IDs are provided");
    }

    @Test
    public void testGetStudentsAttendingAllEvents_NoStudentAttendedAll() {
        List<Long> eventIds = List.of(1L, 2L);

        // Create dummy students
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        // Create attendances such that each student attended only one event
        EventAttendance attendance1 = new EventAttendance();
        attendance1.setStudent(student1);
        EventAttendance attendance2 = new EventAttendance();
        attendance2.setStudent(student2);

        List<EventAttendance> attendances = List.of(attendance1, attendance2);
        when(eventAttendanceRepository.findByEventIdIn(eventIds)).thenReturn(attendances);

        List<Student> students = eventService.getStudentsAttendingAllEvents(eventIds);
        assertTrue(students.isEmpty(), "Expected no students to have attended all events");
    }

    @Test
    public void testGetStudentsAttendingAllEvents_StudentAttendedAll() {
        List<Long> eventIds = List.of(1L, 2L);

        // Create dummy students
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        // student1 attends both events, student2 attends only one event
        EventAttendance attendance1 = new EventAttendance();
        attendance1.setStudent(student1);
        EventAttendance attendance2 = new EventAttendance();
        attendance2.setStudent(student1);
        EventAttendance attendance3 = new EventAttendance();
        attendance3.setStudent(student2);

        List<EventAttendance> attendances = List.of(attendance1, attendance2, attendance3);
        when(eventAttendanceRepository.findByEventIdIn(eventIds)).thenReturn(attendances);

        List<Student> students = eventService.getStudentsAttendingAllEvents(eventIds);
        assertEquals(1, students.size(), "Expected only one student to have attended all events");
        assertTrue(students.contains(student1), "Student1 should have attended all events");
        assertFalse(students.contains(student2), "Student2 should not be included as they did not attend all events");
    }
}
