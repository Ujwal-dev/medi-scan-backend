package com.mediscan.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Services.LoginService;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    
    @Autowired
    private LoginService loginService;

    @PostMapping("/authenticate")
     public ResponseEntity<String> authenticateProfile(@RequestBody Login login) {
        int response = loginService.validateUser(login);
        if(response == 2)
        return ResponseEntity.status(HttpStatus.OK).body("Valid User!!");
        if(response == 0 || response == 1)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not Registered!!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incorrect Password!!");
    }
}
