package com.example.bi.controllers;

import com.example.bi.model.SignUpRequestModel;
import com.example.bi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class User {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello!");
    }

    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<?> testing(){
        return ResponseEntity.ok("testing");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestModel request) {
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Password reset email sent");
    }
}
