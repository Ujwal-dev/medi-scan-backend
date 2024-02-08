package com.mediscan.backend.Controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.OTP;
import com.mediscan.backend.Services.ForgetPasswordService;

import jakarta.mail.MessagingException;

@RestController
@CrossOrigin(origins = "*")
public class ForgetPasswordController {
    @Autowired
    private ForgetPasswordService forgetPasswordService;
    
    @PostMapping("/forgot-password/otp")
    public ResponseEntity<String> generateOTP(@RequestBody Login login) throws MailException, UnsupportedEncodingException, MessagingException {
        if (!forgetPasswordService.exists(login.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not registered!");
        }
        forgetPasswordService.sendOTP(login.getEmail());
        return ResponseEntity.ok("OTP sent successfully");
    }
    @PostMapping("forget-password/validate-otp")    
    public ResponseEntity<String> validateOtp(@RequestBody OTP otpBody) {
        int isValid = forgetPasswordService.validateOTP(otpBody.getEmail(), otpBody.getOTP());
        if (isValid == 0) {
            forgetPasswordService.setChangePasswordEnabled(otpBody.getEmail());
            return ResponseEntity.ok("OTP is valid!");
        } else if (isValid == 2) {
            return ResponseEntity.badRequest().body("Invalid OTP!");
        } else if (isValid == 1) {
            return ResponseEntity.badRequest().body("Time expired for OTP!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred!");
    }
    @PostMapping("/forget-password/set-new-password")
    public ResponseEntity<String> changePassword(@RequestBody Login login) {
        if(!forgetPasswordService.canChangePassword(login.getEmail())) {
            return ResponseEntity.badRequest().body("Cannot change password! Reinitiate the request for changing password!");
        }
        forgetPasswordService.changePassword(login.getEmail(),login.getPassword());
        return ResponseEntity.ok("Password changed successfully!");
    }
}
