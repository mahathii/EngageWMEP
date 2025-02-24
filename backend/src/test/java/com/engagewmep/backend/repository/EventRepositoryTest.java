package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a new Event and set its properties (e.g., eventDate)
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 12, 31));

        // Save the event
        Event savedEvent = eventRepository.save(event);

        // Retrieve the event by its ID
        Optional<Event> retrievedEvent = eventRepository.findById(savedEvent.getId());
        assertThat(retrievedEvent).isPresent();
        assertThat(retrievedEvent.get().getEventDate()).isEqualTo(LocalDate.of(2025, 12, 31));
    }

    @Test
    public void testFindAll() {
        // Create and save multiple events
        Event event1 = new Event();
        event1.setEventDate(LocalDate.of(2025, 1, 1));
        Event event2 = new Event();
        event2.setEventDate(LocalDate.of(2025, 2, 1));

        eventRepository.save(event1);
        eventRepository.save(event2);

        // Retrieve all events and verify
        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDelete() {
        // Create and save an event
        Event event = new Event();
        event.setEventDate(LocalDate.of(2025, 3, 15));
        Event savedEvent = eventRepository.save(event);
        Long eventId = savedEvent.getId();

        // Delete the event
        eventRepository.delete(savedEvent);

        // Verify the event has been deleted
        Optional<Event> deletedEvent = eventRepository.findById(eventId);
        assertThat(deletedEvent).isNotPresent();
    }
}
