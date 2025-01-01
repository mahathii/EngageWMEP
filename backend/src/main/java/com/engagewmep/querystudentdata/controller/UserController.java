package com.engagewmep.querystudentdata.controller;

import com.engagewmep.querystudentdata.dto.RegisterDto;
import com.engagewmep.querystudentdata.dto.LoginDto;
import com.engagewmep.querystudentdata.model.PasswordResetToken;
import com.engagewmep.querystudentdata.model.UserEntity;
import com.engagewmep.querystudentdata.model.VerificationToken;
import com.engagewmep.querystudentdata.repository.UserRepository;
import com.engagewmep.querystudentdata.service.VerificationTokenService;
import com.engagewmep.querystudentdata.service.PasswordResetTokenService;
import com.engagewmep.querystudentdata.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    // Login Endpoint
    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        boolean userExists = userRepository.existsByEmail(loginDto.getEmail());

        if (userExists) {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(loginDto.getEmail());
            UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getStatus().equals("ACTIVE")) {
                return new ResponseEntity<>("Email not verified. Please check your inbox.", HttpStatus.UNAUTHORIZED);
            }

            if (loginDto.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok().body("Logged in successfully!");
            } else {
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Register Endpoint
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is already registered!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setStatus("PENDING"); // User status is pending until email is verified

        userRepository.save(user);

        // Generate and send verification token
        VerificationToken token = tokenService.createVerificationToken(user);
        emailService.sendVerificationEmail(user.getEmail(), token.getToken());

        return new ResponseEntity<>("User registered successfully! Please verify your email.", HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        UserEntity user = userOptional.get();

        // Create password reset token
        PasswordResetToken token = passwordResetTokenService.createToken(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());

        return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
    }

    // Reset Password Endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String tokenStr = request.get("token");
        String newPassword = request.get("password");

        Optional<PasswordResetToken> tokenOptional = passwordResetTokenService.findByToken(tokenStr);

        if (!tokenOptional.isPresent()) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }

        PasswordResetToken token = tokenOptional.get();

        if (token.isExpired()) {
            return new ResponseEntity<>("Token has expired", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = token.getUser();
        user.setPassword(newPassword); // You should hash the password in a real application
        userRepository.save(user);

        // Invalidate the token after successful password reset
        passwordResetTokenService.deleteToken(token);

        return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
    }


    // Email Verification Endpoint
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = tokenService.findByToken(token);

        if (verificationToken.isPresent()) {
            UserEntity user = verificationToken.get().getUser();

            // Check if token has expired (assuming we added an expiry date in the VerificationToken)
            if (System.currentTimeMillis() > verificationToken.get().getExpiryDate()) {
                return new ResponseEntity<>("Token expired. Please request a new verification email.", HttpStatus.BAD_REQUEST);
            }

            user.setStatus("ACTIVE");
            userRepository.save(user);

            return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }
    }
}
