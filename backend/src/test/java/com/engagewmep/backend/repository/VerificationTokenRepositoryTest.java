package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.VerificationToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByToken_Found() {
        // Arrange: Create and persist a VerificationToken entity with a specific token value.
        VerificationToken token = new VerificationToken();
        token.setToken("sample-token");
        // Persist the token in the in-memory database.
        token = entityManager.persistAndFlush(token);

        // Act: Retrieve the token by its token value.
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken("sample-token");

        // Assert: Verify that the token is found and the token value matches.
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getToken()).isEqualTo("sample-token");
    }

    @Test
    public void testFindByToken_NotFound() {
        // Act: Attempt to find a token that does not exist.
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken("non-existent-token");

        // Assert: Verify that no token is found.
        assertThat(foundToken).isNotPresent();
    }
}
