package com.engagewmep.backend.service;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import com.engagewmep.backend.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EventAttendanceRepository eventAttendanceRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetEventsByStudentStudentId() {
        String studentId = "S001";

        // Create dummy events
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(2L);

        // Create a dummy student with the given studentId
        Student student = new Student();
        student.setStudentId(studentId);

        // Create event attendance records linking the student to events
        EventAttendance attendance1 = new EventAttendance();
        attendance1.setEvent(event1);
        attendance1.setStudent(student);
        EventAttendance attendance2 = new EventAttendance();
        attendance2.setEvent(event2);
        attendance2.setStudent(student);

        List<EventAttendance> attendances = Arrays.asList(attendance1, attendance2);

        when(eventAttendanceRepository.findByStudentStudentId(studentId)).thenReturn(attendances);

        List<Event> result = studentService.getEventsByStudentStudentId(studentId);

        assertNotNull(result, "The returned event list should not be null");
        assertEquals(2, result.size(), "Expected two events to be returned");
        assertTrue(result.contains(event1), "The result should contain event1");
        assertTrue(result.contains(event2), "The result should contain event2");

        verify(eventAttendanceRepository, times(1)).findByStudentStudentId(studentId);
    }

    @Test
    public void testGetStudentsByTimeFrame() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        // Create dummy students
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        // Create event attendance records. Duplicate attendance for student1 to test distinct behavior.
        EventAttendance attendance1 = new EventAttendance();
        attendance1.setStudent(student1);
        attendance1.setEvent(new Event());
        EventAttendance attendance2 = new EventAttendance();
        attendance2.setStudent(student1);
        attendance2.setEvent(new Event());
        EventAttendance attendance3 = new EventAttendance();
        attendance3.setStudent(student2);
        attendance3.setEvent(new Event());

        List<EventAttendance> attendances = Arrays.asList(attendance1, attendance2, attendance3);

        when(eventAttendanceRepository.findByEventDateBetween(startDate, endDate)).thenReturn(attendances);

        List<Student> result = studentService.getStudentsByTimeFrame(startDate, endDate);

        // Verify that distinct students are returned
        assertNotNull(result, "The returned student list should not be null");
        assertEquals(2, result.size(), "Expected two distinct students to be returned");
        assertTrue(result.contains(student1), "The result should contain student1");
        assertTrue(result.contains(student2), "The result should contain student2");

        verify(eventAttendanceRepository, times(1)).findByEventDateBetween(startDate, endDate);
    }
}
