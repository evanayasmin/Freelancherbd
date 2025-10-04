package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private CategoryRepository categoryRepository;
    private UserProfileRepository userProfileRepository;

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @ModelAttribute("loggedUser")
    public CustomUserDetail loggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUserDetail) {
                return (CustomUserDetail) principal; // includes profile picture, name, email, etc.
            }
        }
        return null;

    }

}
