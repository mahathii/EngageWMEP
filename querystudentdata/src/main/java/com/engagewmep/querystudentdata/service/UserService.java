package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.UserEntity;
import com.engagewmep.querystudentdata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
