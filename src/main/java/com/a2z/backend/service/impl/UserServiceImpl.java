package com.a2z.backend.service.impl;

import com.a2z.backend.entity.Role;
import com.a2z.backend.entity.User;
import com.a2z.backend.repository.UserRepository;
import com.a2z.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {

        // Check if email already exists
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already registered");
                });

        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }

        // 🔐 Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        // Authentication is now handled by Spring Security
        // This method just returns the user details
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
}
