package com.engagewmep.querystudentdata.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private long expiryDate;
}
