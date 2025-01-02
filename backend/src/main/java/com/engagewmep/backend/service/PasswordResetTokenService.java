package com.engagewmep.backend.service;

import com.engagewmep.backend.model.PasswordResetToken;
import com.engagewmep.backend.model.UserEntity;
import com.engagewmep.backend.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(UserEntity user) {
        // Generate a unique token
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        return tokenRepository.save(passwordResetToken);
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}
