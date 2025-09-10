package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserProfileController {

    private UserProfileService userProfileService;

    @Autowired
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<UserProfile> createProfile(@RequestParam String country,
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

        String cvPath = "/uploads/cv/" + cv.getOriginalFilename();
        String profilePicPath = "/uploads/profile/" + profile_picture.getOriginalFilename();

        UserProfile profile = userProfileService.CreateUserProfile(city, country, gender, skills, cvPath,
                github, linkedin, company_name, company_address, company_email, company_business, company_phone, company_url, profilePicPath);

        return ResponseEntity.ok(profile);

    }

}
