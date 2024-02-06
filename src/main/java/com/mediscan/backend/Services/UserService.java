package com.mediscan.backend.Services;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mediscan.backend.Components.Login;
import com.mediscan.backend.Components.Register;
import com.mediscan.backend.Components.User;
import com.mediscan.backend.Repositories.UserRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validateUser(Login login){
        if(!userRepo.existsByEmail(login.getEmail()))
        return false;
        User user = userRepo.findByEmail(login.getEmail()).get();
        return passwordEncoder.matches(login.getPassword(), user.getPassword());
    }
    
    public int registerUser(Register register)
        throws UnsupportedEncodingException, MessagingException 
    {
        Optional<User> optUser = userRepo.findByEmail(register.getEmail());
        User user = optUser.isPresent()?optUser.get():new User(null,register.getUsername(),register.getEmail(),null,null,false,null);
        if(userRepo.existsByEmail(register.getEmail())) {
            if(user.getEnabled())
                return 1; // user already registered and verified
        }
        String encodedPassword = passwordEncoder.encode(register.getPassword());
        String generatedCode = getCode();
    
        sendVerificationEmail(generatedCode , register.getEmail());
        String encodedGeneratedCode = passwordEncoder.encode(generatedCode);
        user.setPassword(encodedPassword);
        user.setVerificationCode(encodedGeneratedCode);
        user.setCreatedTime(new Date());
        userRepo.save(user);
        return 0;
    }

    public String getCode() {
        int leftLimit = '0'; // letter 'a'
        int rightLimit = '9'; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
    private void sendVerificationEmail(String verificationCode, String toAddress) 
        throws MessagingException, UnsupportedEncodingException{
            
        if (!isValidEmail(toAddress)) {
            System.out.println("Invalid email address: " + toAddress);
            return;
        }
        String subject = "Please verify your registration";
        String content = "<h2>Dear User,</h2>" +
                "<h3><strong>Your one-time password:</strong> " + verificationCode + "</h3>" +
                "<p>Please note that the OTP is valid only for 3 minutes. If you try to refresh the page or leave the site, you will be required to regenerate a new OTP.</p><br><br>" +
                "<p>If you did not request this OTP, please connect with us immediately at mediscan.team.official@gmail.com.</p>" +
                "<p>Regards,<br>" +
                "Support Team<br>" +
                "MediScan<br>" +
                "mail us at : mediscan.team.official@gmail.com</p>";


        sendEmail(toAddress, subject, content);
        System.out.println("\n\n\nSent email\n\n\n");
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); 
            mailSender.send(message);
        }   
    // Method to validate email address format using regular expression
    private boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public long timeDiffernce(Date startDate, Date endDate) {
        // Get the time in milliseconds from each Date object
        Instant instant = startDate.toInstant();
        startDate = Date.from(instant);
        long startTimeMillis = startDate.getTime();
        long endTimeMillis = endDate.getTime();

        // Calculate the time difference in milliseconds
        long timeDifferenceMillis = endTimeMillis - startTimeMillis;

        // Convert the time difference from milliseconds to seconds
        return timeDifferenceMillis / 1000;
    }

    public int validateOtp(String email, String otp) {
        User user = userRepo.findByEmail(email).get();
        long timeDiff = timeDiffernce(user.getCreatedTime(), new Date());
        if(timeDiff > 180)
        return 1; // otp validation exceeds 3 minutes
        if(!passwordEncoder.matches(otp, user.getVerificationCode()))
        return 2; // If the otp is invalid
        user.setEnabled(true);
        userRepo.save(user);
        return 0; // If the otp is valid
    }
}