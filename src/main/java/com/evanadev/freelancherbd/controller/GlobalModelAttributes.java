package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(GlobalModelAttributes.class);

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @ModelAttribute("loggedUser")
    public CustomUserDetail loggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

       /* if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUserDetail) {
                return (CustomUserDetail) principal; // includes profile picture, name, email, etc.
            }
        }
        return null;*/
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Object principal = auth.getPrincipal();
            log.info("Auth Principal Class: {}", principal.getClass().getName());

            if (principal instanceof CustomUserDetail) {
                CustomUserDetail cud = (CustomUserDetail) principal;
                log.info("Principal username: {}", cud.getUsername());
                log.info("Has user object: {}", cud.getUser() != null);
                return cud;
            }
        }
        return null;

    }

}
