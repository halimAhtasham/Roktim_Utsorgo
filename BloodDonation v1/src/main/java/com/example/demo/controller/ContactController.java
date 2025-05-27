package com.example.demo.controller;

import com.example.demo.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/contact/send")
    public String handleContactForm(
            @RequestParam("user_name") String userName,
            @RequestParam("user_email") String userEmail,
            @RequestParam("email_subject") String subject,
            @RequestParam("email_message") String messageText,
            RedirectAttributes redirectAttributes) {

        emailService.sendMessage(userName, userEmail, subject, messageText);

        redirectAttributes.addFlashAttribute("message", "Your message has been sent successfully!");
        return "redirect:/contact";
    }

}