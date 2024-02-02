package com.mediscan.backend.Services;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Components.User;

@Service
public class RegisterService extends UserService {
    public int registerUser(Register register)
    {
        Optional<User> optUser = userRepo.findByEmail(register.getEmail());
        User user = optUser.isPresent()?optUser.get():new User(null,register.getUsername(),register.getEmail(),null,null,false,null,false);
        if(userRepo.existsByEmail(register.getEmail())) {
            if(user.getEnabled())
                return 1; // user already registered and otp is verified
        }
        String encodedPassword = passwordEncoder.encode(register.getPassword());
        
        user.setPassword(encodedPassword);
        userRepo.save(user);
        try {
            sendOTP(user.getEmail());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
