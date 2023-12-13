package com.mediscan.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.OTP;
import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Services.UserService;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getMethodName() {
        return "Hello World!";
    }
    
    @PostMapping("/authenticate")
     public ResponseEntity<String> authenticateProfile(@RequestBody Login login) {
        if(userService.validateUser(login))
        return ResponseEntity.status(HttpStatus.OK).body("Valid User!!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials");
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Register register) 
        throws UnsupportedEncodingException, MessagingException {
        
        if(! register.getPassword().equals(register.getConfirmPassword()))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incorrect password!");
        int result = userService.registerUser(register);
        if(result == 1)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User already registered!");
        if(result == 2)
        return ResponseEntity.status(301).body("Wait before sending another request!");
        return ResponseEntity.status(HttpStatus.OK).body("Email sent Successfully");
    }
    
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody OTP otpBody ) {

        // Validate the OTP
        int isValid = userService.validateOtp(otpBody.getEmail(), otpBody.getOTP());

        if (isValid == 0) {
            return new ResponseEntity<>("OTP is valid!", HttpStatus.OK);
        } else if(isValid == 2){
            return new ResponseEntity<>("Invalid OTP!", HttpStatus.BAD_REQUEST);
        }
        else if(isValid == 1) {

            return new ResponseEntity<>("Time Expired!", HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>("Some error has occured!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
