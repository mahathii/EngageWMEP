package com.engagewmep.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class PasswordResetToken {

    // Token validity duration: e.g., 24 hours
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, UserEntity user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    // Helper method to calculate expiry date
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    // Check if the token is expired
    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}
