package com.mediscan.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.User;
import com.mediscan.backend.Repositories.UserRepo;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    protected UserRepo userRepo;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected EmailService emailService;

    public void sendOTP(String email) throws UnsupportedEncodingException, MailException, MessagingException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String generatedCode = EmailUtil.generateOTP();
        emailService.sendEmail(email, "MediScan Verification", generateEmailContent(generatedCode));
        user.setVerificationCode(passwordEncoder.encode(generatedCode));
        user.setCreatedTime(Date.from(Instant.now()));
        userRepo.save(user);
    }

    private String generateEmailContent(String verificationCode) {
        return "<h2>Dear User,</h2>" +
                "<h3><strong>Your one-time password:</strong> " + verificationCode + "</h3>" +
                "<p>Please note that the OTP is valid only for 3 minutes. If you try to refresh the page or leave the site, you will be required to regenerate a new OTP.</p><br><br>" +
                "<p>If you did not request this OTP, please connect with us immediately at mediscan.team.official@gmail.com.</p>" +
                "<p>Regards,<br>" +
                "Support Team<br>" +
                "MediScan<br>" +
                "mail us at : mediscan.team.official@gmail.com</p>";
    }

    public int validateOTP(String email, String otp) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        long timeDiff = getTimeDifference(user.getCreatedTime(), new Date());
        if (timeDiff > 180) return 1; // otp validation exceeds 3 minutes
        if (!passwordEncoder.matches(otp, user.getVerificationCode())) return 2; // If the otp is invalid
        user.setEnabled(true);
        userRepo.save(user);
        return 0; // If the otp is valid
    }

    protected long getTimeDifference(Date startDate, Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant()).getSeconds();
    }
}
