package com.engagewmep.backend.service;

import com.engagewmep.backend.model.UserEntity;
import com.engagewmep.backend.model.VerificationToken;
import com.engagewmep.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public VerificationToken createVerificationToken(UserEntity user) {
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        // Add an expiry date logic (e.g., 24 hours expiry)
        token.setExpiryDate(System.currentTimeMillis() + 86400000); // 24 hours
        return tokenRepository.save(token);
    }

    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
