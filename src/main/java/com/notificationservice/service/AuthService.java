package com.notificationservice.service;

import com.notificationservice.model.dto.AuthRequest;
import com.notificationservice.model.dto.AuthResponse;
import com.notificationservice.model.entity.User;
import com.notificationservice.repository.UserRepository;
import com.notificationservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Authentication Service for JWT-based authentication
 * <p>
 * This service handles:
 * - User authentication and JWT token generation
 * - User registration
 * - Token validation
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticate user and generate JWT token
     */
    public AuthResponse authenticate(AuthRequest request) {
        try {
            logger.info("Authenticating user: {}", request.getUsername());

            // Find user by username
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = userOpt.get();

            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            return new AuthResponse(token, "Bearer", user.getUsername(), "Authentication successful");

        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Register new user
     */
    public AuthResponse register(AuthRequest request) {
        try {
            logger.info("Registering new user: {}", request.getUsername());

            // Check if user already exists
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);

            userRepository.save(user);

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            logger.info("User registered successfully: {}", request.getUsername());

            return new AuthResponse(token, "Bearer", user.getUsername(), "Registration successful");

        } catch (Exception e) {
            logger.error("Registration failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            return jwtUtil.validateToken(token, username);
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false;
        }
    }

    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}