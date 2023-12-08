package com.mediscan.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Components.User;
import com.mediscan.backend.Repositories.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public boolean validateUser(Login login){
        if(!userRepo.existsByEmail(login.getEmail()))
        return false;
        User user = userRepo.findByEmail(login.getEmail()).get();
        return login.getPassword().equals(user.getPassword());
    }
    
    public User registerUser(Register register)
    {
        if(userRepo.existsByEmail(register.getEmail()))
        return null;
        User user = new User(null, register.getUsername(), register.getEmail(), register.getPassword());
        return userRepo.insert(user);
    }
}
