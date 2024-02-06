package com.mediscan.backend.Configs;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;
    @Value("${spring.mail.port}")
    private String mailPort;
    
    @Bean
    public JavaMailSender getJavaMailSender(){
        System.out.println("\n\n\n" + mailHost + mailPassword + mailUsername + mailPort + "\n\n\n");
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.parseInt(mailPort));
        javaMailSender.setPassword(mailPassword);
        javaMailSender.setUsername(mailUsername);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable","true");
        return javaMailSender;
    }
}
