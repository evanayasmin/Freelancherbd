package com.evanadev.freelancherbd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class ContactController {


    private final JavaMailSender javaMailSender;

    public ContactController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @GetMapping("/user/support/contact-us")
    public String contactPage() {
        return "contact_us";
    }

    @PostMapping("/user/support/contact")
    public String sendContactEmail(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message, Model model) {

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("admin@freelancingbd.com"); // Admin email
            mail.setSubject("Contact Us - " + subject);
            mail.setText(
                    "Name: " + name + "\n" +
                            "Email: " + email + "\n\n" +
                            "Message:\n" + message
            );

            javaMailSender.send(mail);
            model.addAttribute("successMessage",
                    "Your message has been sent successfully!");

        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "Failed to send message. Please try again later.");
        }

        return "contact_us";
    }
}

