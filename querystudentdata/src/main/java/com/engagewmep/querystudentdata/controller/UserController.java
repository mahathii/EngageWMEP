package com.engagewmep.querystudentdata.controller;

import com.engagewmep.querystudentdata.dto.RegisterDto;
import com.engagewmep.querystudentdata.dto.LoginDto;
import com.engagewmep.querystudentdata.model.UserEntity;
import com.engagewmep.querystudentdata.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        boolean userExists = userRepository.existsByUsername(loginDto.getUsername());
        if (userExists) {
            return ResponseEntity.ok().body("Login acknowledged for username: " + loginDto.getUsername() + ". No actual authentication performed.");
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        System.out.println("Register endpoint hit with username: " + registerDto.getUsername());
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
