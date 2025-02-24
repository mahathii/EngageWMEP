package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.PasswordResetToken;
import com.engagewmep.backend.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByToken_Found() {
        // Arrange: Create and persist a dummy UserEntity
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user = entityManager.persistAndFlush(user);

        // Create a PasswordResetToken with a specific token value
        String tokenValue = "token123";
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token = tokenRepository.save(token);
        entityManager.flush();

        // Act: Retrieve the token by token value
        Optional<PasswordResetToken> foundToken = tokenRepository.findByToken(tokenValue);

        // Assert: Verify the token is present and its token value matches
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getToken()).isEqualTo(tokenValue);
    }

    @Test
    public void testFindByToken_NotFound() {
        // Act: Try to retrieve a token that does not exist
        Optional<PasswordResetToken> foundToken = tokenRepository.findByToken("nonexistent-token");

        // Assert: Verify that no token is found
        assertThat(foundToken).isEmpty();
    }

    @Test
    public void testFindByUser_Found() {
        // Arrange: Create and persist a dummy UserEntity
        UserEntity user = new UserEntity();
        user.setUsername("anotheruser");
        user.setEmail("another@example.com");
        user = entityManager.persistAndFlush(user);

        // Create a PasswordResetToken for the user
        String tokenValue = "token456";
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token = tokenRepository.save(token);
        entityManager.flush();

        // Act: Retrieve the token by the user entity
        Optional<PasswordResetToken> foundToken = tokenRepository.findByUser(user);

        // Assert: Verify that the token is present and the token value matches
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getToken()).isEqualTo(tokenValue);
    }

    @Test
    public void testFindByUser_NotFound() {
        // Arrange: Create and persist a dummy UserEntity with no associated token
        UserEntity user = new UserEntity();
        user.setUsername("nonexistinguser");
        user.setEmail("nonexisting@example.com");
        user = entityManager.persistAndFlush(user);

        // Act: Try to retrieve a token for this user
        Optional<PasswordResetToken> foundToken = tokenRepository.findByUser(user);

        // Assert: Verify that no token is found for the user
        assertThat(foundToken).isEmpty();
    }
}
