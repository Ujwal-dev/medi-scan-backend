package com.mediscan.backend.Services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.User;

@Service
public class ForgetPasswordService extends UserService {
    public boolean exists(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean canChangePassword(String email) {
        User user = userRepo.findByEmail(email).get();
        long timeDiff = getTimeDifference(user.getCreatedTime(), new Date());
        if(user.getChangePasswordEnabled() && timeDiff < (60*3)) {
            return true;
        }
        
        return false;
    }

    public void changePassword(String email , String password) {
        User user = userRepo.findByEmail(email).get();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setChangePasswordEnabled(false);
        userRepo.save(user);
    }

    public void setChangePasswordEnabled(String email) {
        User user = userRepo.findByEmail(email).get();
        user.setChangePasswordEnabled(true);
        user.setCreatedTime(Date.from(Instant.now()));
        userRepo.save(user);
    }
}
