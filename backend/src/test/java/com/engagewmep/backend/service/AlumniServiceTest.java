package com.engagewmep.backend.service;

import com.engagewmep.backend.model.Alumni;
import com.engagewmep.backend.repository.AlumniRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlumniServiceTest {

    @Mock
    private AlumniRepository alumniRepository;

    @InjectMocks
    private AlumniService alumniService;

    @Test
    public void testSaveOrUpdateAlumni() {
        Alumni alumni = new Alumni();
        // Optionally set properties on the alumni object

        when(alumniRepository.save(alumni)).thenReturn(alumni);
        Alumni result = alumniService.saveOrUpdateAlumni(alumni);
        assertEquals(alumni, result, "The saved alumni should be returned");
        verify(alumniRepository, times(1)).save(alumni);
    }

    @Test
    public void testFindAllAlumni() {
        Alumni alumni1 = new Alumni();
        Alumni alumni2 = new Alumni();
        List<Alumni> alumniList = Arrays.asList(alumni1, alumni2);

        when(alumniRepository.findAll()).thenReturn(alumniList);
        List<Alumni> result = alumniService.findAllAlumni();
        assertEquals(2, result.size(), "There should be two alumni returned");
        verify(alumniRepository, times(1)).findAll();
    }

    @Test
    public void testFindAlumniById_Found() {
        Alumni alumni = new Alumni();
        when(alumniRepository.findById(1L)).thenReturn(Optional.of(alumni));
        Alumni result = alumniService.findAlumniById(1L);
        assertNotNull(result, "Alumni should be found for a valid ID");
        verify(alumniRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAlumniById_NotFound() {
        when(alumniRepository.findById(1L)).thenReturn(Optional.empty());
        Alumni result = alumniService.findAlumniById(1L);
        assertNull(result, "Null should be returned when no alumni is found");
        verify(alumniRepository, times(1)).findById(1L);
    }

    @Test
    public void testSearchAlumni() {
        String searchTerm = "test@example.com";
        Alumni alumni = new Alumni();
        List<Alumni> alumniList = Collections.singletonList(alumni);

        when(alumniRepository.findByEmailAddress(searchTerm)).thenReturn(alumniList);
        List<Alumni> result = alumniService.searchAlumni(searchTerm);
        assertEquals(1, result.size(), "The search should return one matching alumni");
        verify(alumniRepository, times(1)).findByEmailAddress(searchTerm);
    }

    @Test
    public void testDeleteAlumni() {
        Long id = 1L;
        doNothing().when(alumniRepository).deleteById(id);
        alumniService.deleteAlumni(id);
        verify(alumniRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetFilterValues() {
        // Create two Alumni objects with distinct values for each filterable field
        Alumni alumni1 = new Alumni();
        Alumni alumni2 = new Alumni();

        // Assume that Alumni class has appropriate setters
        alumni1.setRaceEthnicity("Asian");
        alumni2.setRaceEthnicity("Caucasian");

        alumni1.setGender("Male");
        alumni2.setGender("Female");

        alumni1.setNcsuGraduate("Yes");
        alumni2.setNcsuGraduate("No");

        alumni1.setCollegeOfEngineeringGraduate("Yes");
        alumni2.setCollegeOfEngineeringGraduate("Yes");

        alumni1.setYearOfGraduation("2020");
        alumni2.setYearOfGraduation("2021");

        alumni1.setStpParticipationAndYear("2020");
        alumni2.setStpParticipationAndYear("2021");

        alumni1.setMajor("Computer Science");
        alumni2.setMajor("Electrical Engineering");

        alumni1.setCurrentCity("Raleigh");
        alumni2.setCurrentCity("Durham");

        alumni1.setCurrentState("NC");
        alumni2.setCurrentState("NC");

        alumni1.setCurrentZipCode("27607");
        alumni2.setCurrentZipCode("27707");

        alumni1.setMentoringOptIn("Yes");
        alumni2.setMentoringOptIn("No");

        List<Alumni> alumniList = Arrays.asList(alumni1, alumni2);
        when(alumniRepository.findAll()).thenReturn(alumniList);

        Map<String, List<String>> filters = alumniService.getFilterValues();

        // Verify each filter has the expected distinct values
        assertTrue(filters.get("raceEthnicity").containsAll(Arrays.asList("Asian", "Caucasian")));
        assertTrue(filters.get("gender").containsAll(Arrays.asList("Male", "Female")));
        assertTrue(filters.get("ncsuGraduate").containsAll(Arrays.asList("Yes", "No")));
        assertEquals(1, filters.get("collegeOfEngineeringGraduate").size());
        assertTrue(filters.get("collegeOfEngineeringGraduate").contains("Yes"));
        assertTrue(filters.get("yearOfGraduation").containsAll(Arrays.asList("2020", "2021")));
        assertTrue(filters.get("stpParticipationAndYear").containsAll(Arrays.asList("2020", "2021")));
        assertTrue(filters.get("major").containsAll(Arrays.asList("Computer Science", "Electrical Engineering")));
        assertTrue(filters.get("currentCity").containsAll(Arrays.asList("Raleigh", "Durham")));
        assertEquals(1, filters.get("currentState").size());
        assertTrue(filters.get("currentState").contains("NC"));
        assertTrue(filters.get("currentZipCode").containsAll(Arrays.asList("27607", "27707")));
        assertTrue(filters.get("mentoringOptIn").containsAll(Arrays.asList("Yes", "No")));

        verify(alumniRepository, times(1)).findAll();
    }
}
