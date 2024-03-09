package com.engagewmep.querystudentdata.service;

import com.engagewmep.querystudentdata.model.Role;
import com.engagewmep.querystudentdata.model.UserEntity;
import com.engagewmep.querystudentdata.repository.RoleRepository;
import com.engagewmep.querystudentdata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Transactional
    public UserEntity registerNewUser(UserEntity user) {
        // Check if user already exists
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            throw new IllegalStateException("Username already taken");
        });

        // Encrypt the user's password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role to the user
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save and return the new user
        return userRepository.save(user);
    }

    // Method to initialize roles and an admin user at the startup
    @Transactional
    public void initRolesAndUsers() {
        // Initialize roles
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(userRole);
        }

        // Create an admin user if not exists
        userRepository.findByUsername("admin").orElseGet(() -> {
            UserEntity admin = new UserEntity("admin", passwordEncoder.encode("admin123"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole); // Assuming adminRole is now persisted
            admin.setRoles(adminRoles);
            return userRepository.save(admin);
        });
    }
}
