package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String fname, String lname, String username, String email, String password, String country, String city, String address, String phone, String role, String gender, Integer status, Date birthday) {
        userService.RegisterUser(fname,  lname,  username,  email,  password,  country,  city,
                 address,  phone,  role,  gender,  status,  birthday);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
