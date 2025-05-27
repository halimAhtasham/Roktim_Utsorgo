package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(String name, String userEmail, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ahtashamhalim@gmail.com"); // Your inbox
        message.setSubject("New Message from Blood Donation Website: " + subject);
        message.setText("Name: " + name + "\nEmail: " + userEmail + "\n\nMessage:\n" + messageText);
        message.setReplyTo(userEmail); //hit reply, goes to user

        mailSender.send(message);
    }
}
