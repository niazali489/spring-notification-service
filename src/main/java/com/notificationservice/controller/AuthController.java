package com.notificationservice.controller;

import com.notificationservice.model.dto.AuthRequest;
import com.notificationservice.model.dto.AuthResponse;
import com.notificationservice.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentication Controller for JWT-based authentication
 * <p>
 * Provides endpoints for:
 * - User login/authentication
 * - Token generation
 * - User registration
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Authenticate user and generate JWT token
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            logger.info("Login attempt for username: {}", request.getUsername());

            AuthResponse response = authService.authenticate(request);

            logger.info("Login successful for username: {}", request.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Login failed for username: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Authentication failed: " + e.getMessage()
            ));
        }
    }

    /**
     * Register new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        try {
            logger.info("Registration attempt for username: {}", request.getUsername());

            AuthResponse response = authService.register(request);

            logger.info("Registration successful for username: {}", request.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Registration failed for username: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Registration failed: " + e.getMessage()
            ));
        }
    }

    /**
     * Validate JWT token
     * POST /api/auth/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                boolean isValid = authService.validateToken(token);

                if (isValid) {
                    return ResponseEntity.ok(Map.of(
                            "status", "valid",
                            "message", "Token is valid"
                    ));
                }
            }

            return ResponseEntity.badRequest().body(Map.of(
                    "status", "invalid",
                    "message", "Token is invalid or expired"
            ));

        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Token validation failed: " + e.getMessage()
            ));
        }
    }
}