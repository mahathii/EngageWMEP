package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByUsername_Found() {
        // Arrange: create and persist a UserEntity with a username
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        entityManager.persistAndFlush(user);

        // Act: retrieve the user by username
        Optional<UserEntity> foundUser = userRepository.findByUsername("testuser");

        // Assert: verify that the user is found and username matches
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // Act: try to retrieve a non-existent user
        Optional<UserEntity> foundUser = userRepository.findByUsername("nonexistent");

        // Assert: verify that no user is returned
        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void testExistsByUsername() {
        // Arrange: create and persist a UserEntity with a username
        UserEntity user = new UserEntity();
        user.setUsername("uniqueuser");
        user.setEmail("unique@example.com");
        entityManager.persistAndFlush(user);

        // Act & Assert: check existence for an existing username
        Boolean exists = userRepository.existsByUsername("uniqueuser");
        assertThat(exists).isTrue();

        // And check for a non-existent username
        Boolean notExists = userRepository.existsByUsername("nonexistentuser");
        assertThat(notExists).isFalse();
    }

    @Test
    public void testExistsByEmail() {
        // Arrange: create and persist a UserEntity with an email
        UserEntity user = new UserEntity();
        user.setUsername("emailuser");
        user.setEmail("email@example.com");
        entityManager.persistAndFlush(user);

        // Act & Assert: check existence for an existing email
        boolean exists = userRepository.existsByEmail("email@example.com");
        assertThat(exists).isTrue();

        // And check for a non-existent email
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(notExists).isFalse();
    }

    @Test
    public void testFindByEmail() {
        // Arrange: create and persist a UserEntity with an email
        UserEntity user = new UserEntity();
        user.setUsername("emailfinder");
        user.setEmail("findme@example.com");
        entityManager.persistAndFlush(user);

        // Act: retrieve the user by email
        Optional<UserEntity> foundUser = userRepository.findByEmail("findme@example.com");

        // Assert: verify that the user is found and the email matches
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("findme@example.com");
    }
}
