package com.mediscan.backend.Controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Services.RegisterService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin(origins = "*")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Register register)
            throws UnsupportedEncodingException, MessagingException {
        if (!register.getPassword().equals(register.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match!");
        }
        int result = registerService.registerUser(register);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already registered!");
        } else if (result == 2) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Wait before sending another request!");
        }
        return ResponseEntity.ok("Registration successful");
    }
}
