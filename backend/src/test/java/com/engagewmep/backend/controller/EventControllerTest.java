package com.engagewmep.backend.controller;

import com.engagewmep.backend.config.TestSecurityConfig;
import com.engagewmep.backend.model.Event;
import com.engagewmep.backend.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.test.context.ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventRepository eventRepository;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll(); // Clear database before each test
        testEvent = new Event();
        testEvent.setName("Test Event");
        testEvent.setEventDate(LocalDate.now().plusDays(5));
        testEvent.setEventTime(LocalTime.of(14, 0));
        testEvent.setEventLocation("Test Location");
        eventRepository.save(testEvent);
    }

    @Test
    void testGetAllEvents() {
        ResponseEntity<Event[]> response = restTemplate.getForEntity("/events", Event[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetPastEvents() {
        testEvent.setEventDate(LocalDate.now().minusDays(5));
        eventRepository.save(testEvent);
        ResponseEntity<Event[]> response = restTemplate.getForEntity("/events/past-events", Event[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testAddEvent() {
        Event newEvent = new Event();
        newEvent.setName("New Event");
        newEvent.setEventDate(LocalDate.now().plusDays(10));
        newEvent.setEventTime(LocalTime.of(16, 30));
        newEvent.setEventLocation("New Location");

        ResponseEntity<Event> response = restTemplate.postForEntity("/events", newEvent, Event.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Event");
    }

    @Test
    void testUpdateEvent() {
        testEvent.setName("Updated Event");
        HttpEntity<Event> requestEntity = new HttpEntity<>(testEvent);

        ResponseEntity<Event> response = restTemplate.exchange(
                "/events/" + testEvent.getId(),
                HttpMethod.PUT,
                requestEntity,
                Event.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Event");
    }

    @Test
    void testDeleteEvent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/events/" + testEvent.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(eventRepository.findById(testEvent.getId())).isEmpty();
    }
}
