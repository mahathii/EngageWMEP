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
import java.util.Optional;


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
        boolean userExists = userRepository.existsByEmail(loginDto.getEmail());
        if (userExists) {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(loginDto.getEmail());
            UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

            if (loginDto.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok().body("Logged in successfully! ");
            } else {
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


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

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
