package com.engagewmep.backend.controller;

import com.engagewmep.backend.model.UserEntity;
import com.engagewmep.backend.repository.UserRepository;
import com.engagewmep.backend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Clear database before each test
        testUser = new UserEntity();
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setStatus("ACTIVE");
        userRepository.save(testUser);

        // Mock email service to prevent real email sending
        doNothing().when(emailService).sendVerificationEmail(testUser.getEmail(), "dummy-token");
        doNothing().when(emailService).sendPasswordResetEmail(testUser.getEmail(), "dummy-token");
    }

    @Test
    void testLoginSuccess() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "test@example.com");
        loginRequest.put("password", "password");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Logged in successfully");
    }

    @Test
    void testRegisterUser() {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("email", "newuser@example.com");
        registerRequest.put("username", "newuser");
        registerRequest.put("password", "newpassword");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("User registered successfully");
    }

}
