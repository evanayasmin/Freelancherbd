package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.FileStorageService;
import com.evanadev.freelancherbd.service.UserProfileService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserProfileController {

    private UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public UserProfileController(UserRepository userRepository,
                                 UserProfileRepository userProfileRepository,FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/user_profile")
    public ModelAndView userProfile() {

        return new ModelAndView("user_profile");
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createProfile(@RequestParam String country,
                                                     @RequestParam String city,
                                                     @RequestParam String gender,
                                                     @RequestParam String skills,
                                                     @RequestParam MultipartFile cv,
                                                     @RequestParam String github,
                                                     @RequestParam String linkedin,
                                                     @RequestParam String company_name,
                                                     @RequestParam String company_address,
                                                     @RequestParam String company_email,
                                                     @RequestParam String company_url,
                                                     @RequestParam String company_business,
                                                     @RequestParam String company_phone,
                                                     @RequestParam MultipartFile profile_picture) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();

        //String cvPath = "/uploads/cv/" + cv.getOriginalFilename();
        //String profilePicPath = "/uploads/profile/" + profile_picture.getOriginalFilename();

        // Save files using service
        String cvFilePath = fileStorageService.saveFile(cv, "cv");
        String profilePicturePath = fileStorageService.saveFile(profile_picture, "profile");

        UserProfile profile = userProfileService.CreateUserProfile(city, country, gender, skills, cvFilePath,
                github, linkedin, company_name, company_address, company_email, company_business, company_phone, company_url, profilePicturePath);


        return ResponseEntity.ok("Profile created successfully with uploaded CV & Profile Picture!");
       // return "Ok";

    }

}
