package com.engagewmep.backend.service;

import com.engagewmep.backend.model.UserEntity;
import com.engagewmep.backend.model.VerificationToken;
import com.engagewmep.backend.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository tokenRepository;

    @InjectMocks
    private VerificationTokenService verificationTokenService;

    @Test
    public void testCreateVerificationToken() {
        // Arrange: create a dummy user entity
        UserEntity user = new UserEntity();
        // Stub tokenRepository.save to return the same token it receives
        when(tokenRepository.save(any(VerificationToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: create a verification token for the user
        VerificationToken createdToken = verificationTokenService.createVerificationToken(user);

        // Assert: the created token should have a non-null token string, a valid expiry date, and the correct user association
        assertNotNull(createdToken, "The created token should not be null");
        assertNotNull(createdToken.getToken(), "The token string should not be null");
        assertEquals(user, createdToken.getUser(), "The token's user should match the provided user");

        // Verify that the expiry date is set to roughly 24 hours from now
        long currentTime = System.currentTimeMillis();
        // Allowing a small margin since time passes during execution
        assertTrue(createdToken.getExpiryDate() >= currentTime + 86000000,
                "The expiry date should be roughly 24 hours (86400000 ms) from now");

        // Verify that the repository's save method was invoked once
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    public void testFindByToken_Found() {
        // Arrange: create a dummy token and user
        String tokenStr = "test-token";
        UserEntity user = new UserEntity();
        VerificationToken token = new VerificationToken();
        token.setToken(tokenStr);
        token.setUser(user);

        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        // Act: attempt to find the token
        Optional<VerificationToken> result = verificationTokenService.findByToken(tokenStr);

        // Assert: verify that the token is found and matches expectations
        assertTrue(result.isPresent(), "Token should be found");
        assertEquals(token, result.get(), "Returned token should match the expected token");
        verify(tokenRepository, times(1)).findByToken(tokenStr);
    }

    @Test
    public void testFindByToken_NotFound() {
        // Arrange: setup the repository to return empty for a nonexistent token
        String tokenStr = "nonexistent-token";
        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.empty());

        // Act: attempt to find the token
        Optional<VerificationToken> result = verificationTokenService.findByToken(tokenStr);

        // Assert: verify that the token is not found
        assertFalse(result.isPresent(), "Token should not be found");
        verify(tokenRepository, times(1)).findByToken(tokenStr);
    }
}
