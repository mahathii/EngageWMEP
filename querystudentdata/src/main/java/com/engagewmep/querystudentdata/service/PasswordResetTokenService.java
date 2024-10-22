package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.PasswordResetToken;
import com.engagewmep.querystudentdata.model.UserEntity;
import com.engagewmep.querystudentdata.repository.PasswordResetTokenRepository;
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
