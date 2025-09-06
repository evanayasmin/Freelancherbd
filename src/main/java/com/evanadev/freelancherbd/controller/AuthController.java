package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView  register() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@RequestParam String fullname, String username, String email, String password, String phone, String role, Integer status, Long nid) {
        userService.RegisterUser(fullname, username, email, password, phone, role, status, nid);
        return new ModelAndView("login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/home")
    public ModelAndView home() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        String fullName = userDetails.getFullname();
        String username = userDetails.getUsername();
        String email = userDetails.getEmail();
        String userType = userDetails.getRole();
        String nid = userDetails.getNid().toString();

        ModelAndView mav = new ModelAndView("home");

        mav.addObject("username", username);
        mav.addObject("email", email);
        mav.addObject("role", userType);
        mav.addObject("fullname", fullName);
        mav.addObject("nid", nid);
        return mav;
    }
}
