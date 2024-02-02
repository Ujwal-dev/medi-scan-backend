package com.mediscan.backend.Services;

import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.User;

@Service
public class LoginService extends UserService{
    public int validateUser(Login login){
        if(!userRepo.existsByEmail(login.getEmail()))
        return 0;
        User user = userRepo.findByEmail(login.getEmail()).get();
        if(!user.getEnabled())
        return 1;
        return passwordEncoder.matches(login.getPassword(), user.getPassword()) ? 2:3;
    }
}