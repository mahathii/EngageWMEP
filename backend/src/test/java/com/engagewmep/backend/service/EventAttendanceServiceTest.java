package com.engagewmep.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.model.EventAttendance;
import com.engagewmep.backend.model.Student;
import com.engagewmep.backend.repository.EventAttendanceRepository;
import com.engagewmep.backend.repository.EventRepository;
import com.engagewmep.backend.repository.StudentRepository;
import com.engagewmep.backend.util.ExcelHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventAttendanceServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EventAttendanceRepository eventAttendanceRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventAttendanceService eventAttendanceService;

    // The expected header list, as defined in the service
    private final List<String> expectedHeaders = List.of(
            "student: Student ID",
            "student: Last Name",
            "student: First Name",
            "Student Profile: Degree Level",
            "Student Profile: Graduation Date",
            "Student Profile: Major",
            "Student Profile: College",
            "Student Profile: Admin Major",
            "Student Profile: Email",
            "Student Profile: Ethnicity"
    );

    @Test
    public void testProcessAttendanceFile_Successful() throws Exception {
        // Prepare a dummy file (its content is not used since ExcelHelper is mocked)
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());

        // Create a dummy student with a non-null student ID
        Student student = new Student();
        // Assuming that Student has a setStudentId() method (or an equivalent unique identifier method)
        student.setStudentId("S001");
        List<Student> studentsFromExcel = List.of(student);

        // Create a dummy event
        Event event = new Event();
        event.setId(1L);

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            // Stub ExcelHelper.getExcelHeaders to return the expected headers
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(expectedHeaders);
            // Stub ExcelHelper.parseExcelFile to return our dummy student list
            excelHelperMock.when(() -> ExcelHelper.parseExcelFile(any(InputStream.class)))
                    .thenReturn(studentsFromExcel);

            // Stub eventRepository to return the event
            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            // Stub studentRepository.findByStudentId to simulate a new student (i.e. not found yet)
            when(studentRepository.findByStudentId("S001")).thenReturn(Optional.empty());
            // Stub saving the new student
            when(studentRepository.save(student)).thenReturn(student);
            // Stub attendance existence check: it is checked twice in the method
            when(eventAttendanceRepository.existsByEventAndStudent(event, student)).thenReturn(false);
            // Stub attendance saving to return the attendance object passed
            when(eventAttendanceRepository.save(any(EventAttendance.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Call the method under test
            eventAttendanceService.processAttendanceFile(1L, file);

            // Verify that the event is fetched
            verify(eventRepository, times(1)).findById(1L);
            // Verify that the student lookup and save are performed
            verify(studentRepository, times(1)).findByStudentId("S001");
            verify(studentRepository, times(1)).save(student);
            // Verify that the attendance existence check is called twice
            verify(eventAttendanceRepository, times(2)).existsByEventAndStudent(event, student);
            // Verify that an attendance record is saved
            verify(eventAttendanceRepository, times(1)).save(any(EventAttendance.class));
        }
    }

    @Test
    public void testProcessAttendanceFile_DuplicateAttendance() throws Exception {
        // Prepare a dummy file and student
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());
        Student student = new Student();
        student.setStudentId("S002");
        List<Student> studentsFromExcel = List.of(student);

        // Create a dummy event
        Event event = new Event();
        event.setId(2L);

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(expectedHeaders);
            excelHelperMock.when(() -> ExcelHelper.parseExcelFile(any(InputStream.class)))
                    .thenReturn(studentsFromExcel);

            when(eventRepository.findById(2L)).thenReturn(Optional.of(event));
            // Simulate that the student already exists in the database
            when(studentRepository.findByStudentId("S002")).thenReturn(Optional.of(student));
            // Simulate that an attendance record already exists
            when(eventAttendanceRepository.existsByEventAndStudent(event, student)).thenReturn(true);

            // Expect an exception due to duplicate attendance
            Exception exception = assertThrows(RuntimeException.class, () ->
                    eventAttendanceService.processAttendanceFile(2L, file)
            );
            assertTrue(exception.getMessage().contains("Duplicate entry detected"));
        }
    }

    @Test
    public void testProcessAttendanceFile_NullStudentId() throws Exception {
        // Prepare a dummy file and a student with a null studentId
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());
        Student student = new Student();
        student.setStudentId(null);
        List<Student> studentsFromExcel = List.of(student);

        // Create a dummy event
        Event event = new Event();
        event.setId(3L);

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(expectedHeaders);
            excelHelperMock.when(() -> ExcelHelper.parseExcelFile(any(InputStream.class)))
                    .thenReturn(studentsFromExcel);

            when(eventRepository.findById(3L)).thenReturn(Optional.of(event));

            // Call the method; student with null ID should be skipped.
            eventAttendanceService.processAttendanceFile(3L, file);

            // Verify that no lookup or save is attempted for a null student ID.
            verify(studentRepository, never()).findByStudentId(any());
            verify(studentRepository, never()).save(any());
            verify(eventAttendanceRepository, never()).save(any(EventAttendance.class));
        }
    }

    @Test
    public void testProcessAttendanceFile_EventNotFound() throws Exception {
        // Prepare a dummy file and a student with a valid ID
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());
        Student student = new Student();
        student.setStudentId("S003");
        List<Student> studentsFromExcel = List.of(student);

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(expectedHeaders);
            excelHelperMock.when(() -> ExcelHelper.parseExcelFile(any(InputStream.class)))
                    .thenReturn(studentsFromExcel);

            // Simulate event not found
            when(eventRepository.findById(100L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(RuntimeException.class, () ->
                    eventAttendanceService.processAttendanceFile(100L, file)
            );
            assertTrue(exception.getMessage().contains("Event not found"));
        }
    }

    @Test
    public void testValidateExcelSheet_CorrectHeaders() throws Exception {
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            // Stub ExcelHelper to return the expected headers
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(expectedHeaders);

            // Method should complete without throwing an exception
            assertDoesNotThrow(() -> eventAttendanceService.validateExcelSheet(file));
        }
    }

    @Test
    public void testValidateExcelSheet_IncorrectHeaders() throws Exception {
        String content = "dummy content";
        MultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content.getBytes());
        List<String> wrongHeaders = List.of("Wrong", "Headers");

        try (MockedStatic<ExcelHelper> excelHelperMock = mockStatic(ExcelHelper.class)) {
            excelHelperMock.when(() -> ExcelHelper.getExcelHeaders(any(InputStream.class)))
                    .thenReturn(wrongHeaders);

            Exception exception = assertThrows(RuntimeException.class, () ->
                    eventAttendanceService.validateExcelSheet(file)
            );
            assertTrue(exception.getMessage().contains("Incorrect Excel sheet format"));
        }
    }

}
