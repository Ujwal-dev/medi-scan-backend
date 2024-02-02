package com.mediscan.backend.Controllers;

import com.mediscan.backend.Components.OTP;
import com.mediscan.backend.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String welcomeMessage() {
        return "Hello World!";
    }    

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody OTP otpBody) {
        int isValid = userService.validateOTP(otpBody.getEmail(), otpBody.getOTP());
        if (isValid == 0) {
            return ResponseEntity.ok("OTP is valid!");
        } else if (isValid == 2) {
            return ResponseEntity.badRequest().body("Invalid OTP!");
        } else if (isValid == 1) {
            return ResponseEntity.badRequest().body("Time expired for OTP!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred!");
    }
}
