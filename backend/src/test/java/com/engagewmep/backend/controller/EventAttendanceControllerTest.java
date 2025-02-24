package com.engagewmep.backend.controller;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.service.EventAttendanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventAttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventAttendanceService eventAttendanceService;

    @Test
    public void testUploadAttendance_Successful() throws Exception {
        Long eventId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "attendance.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "dummy-content".getBytes());

        doNothing().when(eventAttendanceService).processAttendanceFile(eq(eventId), any(MockMultipartFile.class));

        mockMvc.perform(multipart("/api/event-attendance/{eventId}/upload", eventId)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Attendance uploaded successfully."));
    }

    @Test
    public void testUploadAttendance_Failed() throws Exception {
        Long eventId = 2L;
        MockMultipartFile file = new MockMultipartFile("file", "attendance.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "dummy-content".getBytes());

        doThrow(new RuntimeException("Event not found")).when(eventAttendanceService)
                .processAttendanceFile(eq(eventId), any(MockMultipartFile.class));

        mockMvc.perform(multipart("/api/event-attendance/{eventId}/upload", eventId)
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found"));
    }

    @Test
    public void testDeleteAttendance_Successful() throws Exception {
        Long eventId = 1L;
        doNothing().when(eventAttendanceService).deleteAttendance(eventId);

        mockMvc.perform(delete("/api/event-attendance/{eventId}/delete", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string("All attendance records deleted successfully."));
    }

    @Test
    public void testDeleteAttendance_EventNotFound() throws Exception {
        Long eventId = 2L;
        doThrow(new RuntimeException("Event not found")).when(eventAttendanceService).deleteAttendance(eventId);

        mockMvc.perform(delete("/api/event-attendance/{eventId}/delete", eventId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found"));
    }

    @Test
    public void testGetEventAttendance_Successful() throws Exception {
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);

        Student student = new Student();
        student.setStudentId("S001");
        student.setFirstName("John");
        student.setLastName("Doe");

        EventAttendance attendance = new EventAttendance();
        attendance.setEvent(event);
        attendance.setStudent(student);

        List<EventAttendance> attendanceList = new ArrayList<>();
        attendanceList.add(attendance);

        when(eventAttendanceService.getEventAttendance(eventId)).thenReturn(attendanceList);

        mockMvc.perform(get("/api/event-attendance/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.studentId").value("S001"))
                .andExpect(jsonPath("$[0].student.firstName").value("John"))
                .andExpect(jsonPath("$[0].student.lastName").value("Doe"));
    }


    @Test
    public void testGetEventAttendance_NoRecords() throws Exception {
        Long eventId = 2L;
        when(eventAttendanceService.getEventAttendance(eventId)).thenReturn(List.of());

        mockMvc.perform(get("/api/event-attendance/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string("No attendance uploaded yet."));
    }

    @Test
    public void testGetEventAttendance_EventNotFound() throws Exception {
        Long eventId = 3L;
        when(eventAttendanceService.getEventAttendance(eventId))
                .thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(get("/api/event-attendance/{eventId}", eventId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Event not found"));
    }
}
