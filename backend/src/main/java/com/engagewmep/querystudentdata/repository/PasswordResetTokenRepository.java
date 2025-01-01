package com.engagewmep.querystudentdata.repository;

import com.engagewmep.querystudentdata.model.PasswordResetToken;
import com.engagewmep.querystudentdata.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(UserEntity user);
}
