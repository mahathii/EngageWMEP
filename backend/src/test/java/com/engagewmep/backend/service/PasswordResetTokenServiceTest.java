package com.engagewmep.backend.service;

import com.engagewmep.backend.model.PasswordResetToken;
import com.engagewmep.backend.model.UserEntity;
import com.engagewmep.backend.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @InjectMocks
    private PasswordResetTokenService tokenService;

    @Test
    public void testCreateToken() {
        // Arrange: create a dummy user entity
        UserEntity user = new UserEntity();
        // Stub repository save method to return the same token it receives
        when(tokenRepository.save(any(PasswordResetToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: create a token for the user
        PasswordResetToken createdToken = tokenService.createToken(user);

        // Assert: verify that the created token is not null, has a valid UUID and correct user association
        assertNotNull(createdToken, "Created token should not be null");
        assertNotNull(createdToken.getToken(), "Token string should not be null");
        assertEquals(user, createdToken.getUser(), "Token's user should match the provided user");
        assertDoesNotThrow(() -> UUID.fromString(createdToken.getToken()), "Token should be a valid UUID");

        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    public void testFindByToken_Found() {
        // Arrange
        String tokenStr = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        PasswordResetToken token = new PasswordResetToken(tokenStr, user);
        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        // Act
        Optional<PasswordResetToken> result = tokenService.findByToken(tokenStr);

        // Assert
        assertTrue(result.isPresent(), "Token should be found");
        assertEquals(token, result.get(), "Returned token should match the expected token");
        verify(tokenRepository, times(1)).findByToken(tokenStr);
    }

    @Test
    public void testFindByToken_NotFound() {
        // Arrange
        String tokenStr = UUID.randomUUID().toString();
        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.empty());

        // Act
        Optional<PasswordResetToken> result = tokenService.findByToken(tokenStr);

        // Assert
        assertFalse(result.isPresent(), "Token should not be found");
        verify(tokenRepository, times(1)).findByToken(tokenStr);
    }

    @Test
    public void testDeleteToken() {
        // Arrange
        String tokenStr = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        PasswordResetToken token = new PasswordResetToken(tokenStr, user);

        // Act
        tokenService.deleteToken(token);

        // Assert: verify that the repository's delete method was invoked with the token
        verify(tokenRepository, times(1)).delete(token);
    }
}
