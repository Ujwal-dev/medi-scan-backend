package com.mediscan.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Components.User;
import com.mediscan.backend.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




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
    public ResponseEntity<String> registerUser(@RequestBody Register register) {
        
        if(! register.getPassword().equals(register.getConfirmPassword()))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incorrect password!");
        User user = userService.registerUser(register);
        if(user == null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User already registered!");

        return ResponseEntity.status(HttpStatus.OK).body("Registered Successfully!\nNow Login to continue");
    }
    
}
