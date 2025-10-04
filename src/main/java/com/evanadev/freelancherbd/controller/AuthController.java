package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    private CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserProfileRepository userProfileRepository;

    public AuthController(CategoryRepository categoryRepository, UserProfileRepository userProfileRepository) {
        this.categoryRepository = categoryRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @GetMapping("/register")
    public ModelAndView  register() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@RequestParam String fullname, @RequestParam String username,
                                     @RequestParam String email, @RequestParam String password,
                                     @RequestParam String phone, @RequestParam String role,
                                     @RequestParam Long nid) {
        userService.RegisterUser(fullname, username, email, password, phone, role, nid);
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
