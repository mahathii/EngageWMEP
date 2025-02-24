package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Alumni;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AlumniRepositoryTest {

    @Autowired
    private AlumniRepository alumniRepository;

    @Test
    public void testFindByEmailAddress_Found() {
        // Given: Create and save an Alumni entity with a specific email address
        Alumni alumni = new Alumni();
        alumni.setEmailAddress("test@example.com");
        alumniRepository.save(alumni);

        // When: Searching for the saved email address
        List<Alumni> result = alumniRepository.findByEmailAddress("test@example.com");

        // Then: Verify that the returned list contains at least one matching entity
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getEmailAddress()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByEmailAddress_NotFound() {
        // When: Searching for an email address that was not saved
        List<Alumni> result = alumniRepository.findByEmailAddress("nonexistent@example.com");

        // Then: Verify that the result is empty
        assertThat(result).isEmpty();
    }

    @Test
    public void testSaveAndFindById() {
        // Given: Create and save an Alumni entity
        Alumni alumni = new Alumni();
        alumni.setEmailAddress("unique@example.com");
        Alumni savedAlumni = alumniRepository.save(alumni);

        // When: Fetching the entity by its generated ID
        Alumni foundAlumni = alumniRepository.findById(savedAlumni.getId()).orElse(null);

        // Then: Verify that the entity is found and its email matches
        assertThat(foundAlumni).isNotNull();
        assertThat(foundAlumni.getEmailAddress()).isEqualTo("unique@example.com");
    }
}
