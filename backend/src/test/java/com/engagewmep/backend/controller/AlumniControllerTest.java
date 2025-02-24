package com.engagewmep.backend.controller;

import com.engagewmep.backend.config.TestSecurityConfig;
import com.engagewmep.backend.model.Alumni;
import com.engagewmep.backend.service.AlumniService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class AlumniControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlumniService alumniService;

    @Test
    public void testAddAndGetAlumni() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setId(1L);
        alumni.setEmailAddress("s001@example.com");
        alumni.setFirstName("John");
        alumni.setLastName("Doe");

        when(alumniService.saveOrUpdateAlumni(any(Alumni.class))).thenReturn(alumni);
        when(alumniService.findAlumniById(1L)).thenReturn(alumni);

        // Perform POST to create alumni
        String postResponse = mockMvc.perform(post("/api/alumni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alumni)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress", is("s001@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andReturn().getResponse().getContentAsString();

        Alumni savedAlumni = objectMapper.readValue(postResponse, Alumni.class);
        Long id = savedAlumni.getId();

        // Perform GET by ID
        mockMvc.perform(get("/api/alumni/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress", is("s001@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void testGetAllAlumni() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setId(2L);
        alumni.setEmailAddress("s002@example.com");
        alumni.setFirstName("Alice");
        alumni.setLastName("Smith");

        when(alumniService.findAllAlumni()).thenReturn(List.of(alumni));

        // Perform GET /api/alumni to retrieve all alumni
        mockMvc.perform(get("/api/alumni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")));
    }

    @Test
    public void testUpdateAlumni() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setId(3L);
        alumni.setEmailAddress("s003@example.com");
        alumni.setFirstName("Bob");
        alumni.setLastName("Marley");

        when(alumniService.findAlumniById(3L)).thenReturn(alumni);
        when(alumniService.saveOrUpdateAlumni(any(Alumni.class))).thenReturn(alumni);

        Alumni updateDetails = new Alumni();
        updateDetails.setFirstName("Robert");
        updateDetails.setLastName("Nesta Marley");

        mockMvc.perform(put("/api/alumni/" + 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Robert")))
                .andExpect(jsonPath("$.lastName", is("Nesta Marley")));
    }

    @Test
    public void testSearchAlumni() throws Exception {
        Alumni alumni = new Alumni();
        alumni.setId(4L);
        alumni.setEmailAddress("s004@example.com");
        alumni.setFirstName("Charlie");
        alumni.setLastName("Brown");

        when(alumniService.searchAlumni("s004@example.com")).thenReturn(List.of(alumni));

        mockMvc.perform(get("/api/alumni/search").param("term", "s004@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Charlie")));
    }

    @Test
    public void testDeleteAlumni() throws Exception {
        when(alumniService.findAlumniById(5L)).thenReturn(new Alumni());

        mockMvc.perform(delete("/api/alumni/" + 5L))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFilterValues() throws Exception {
        mockMvc.perform(get("/api/alumni/filters"))
                .andExpect(status().isOk());
    }
}
