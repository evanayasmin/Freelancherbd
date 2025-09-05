package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView  register() {

        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView  registerUser(@RequestParam String fname, String lname, String username, String email, String password, String country, String city, String address, String phone, String role, String gender, Integer status, Date birthday, Long nid) {
        userService.RegisterUser(fname,  lname,  username,  email,  password,  country,  city,
                 address,  phone,  role,  gender,  status,  birthday, nid);

        return new ModelAndView("login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/home")


    public ModelAndView home() {
         return new ModelAndView("home");
    }
}
