package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.FileStorageService;
import com.evanadev.freelancherbd.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class UserProfileController {

    @Autowired
    private final UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FileStorageService fileStorageService;

    public UserProfileController(UserProfileService userProfileService, UserRepository userRepository,
                                 UserProfileRepository userProfileRepository, FileStorageService fileStorageService) {
        this.userProfileService = userProfileService;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/user_profile")
    public String userProfile(Model model, Authentication authentication) {

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();
        Optional<UserProfile> profile = userProfileRepository.findByUserId(userId);

        if(profile.isPresent()){
            model.addAttribute("profile", profile.get());
        }else{
            model.addAttribute("profile", new UserProfile());
        }
        return "user_profile";
    }

    @PostMapping(value = "/user_profile_submit", consumes = {"multipart/form-data"})
    public String createProfile(@RequestParam String country,
                                                     @RequestParam String city,
                                                     @RequestParam String gender,
                                                     @RequestParam String skills,
                                                     @RequestParam MultipartFile cv,
                                                     @RequestParam String github,
                                                     @RequestParam String linkedin,
                                                     @RequestParam MultipartFile profile_picture, Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Save files using service
        String cvFilePath = fileStorageService.saveFile(cv, "cv");
        String profilePicturePath = fileStorageService.saveFile(profile_picture, "profile");

        // Check if user profile exists
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        String message = "";
        UserProfile profile;
        if (existingProfile.isPresent()) {  // Update existing record
            profile = existingProfile.get();
            profile.setCity(city);
            profile.setCountry(country);
            profile.setGender(gender);
            profile.setSkills(skills);
            profile.setCv(cvFilePath);
            profile.setGithubUrl(github);
            profile.setLinkedinUrl(linkedin);
            profile.setProfilePicture(profilePicturePath);
            profile = userProfileRepository.save(profile);
            message = "User Profile Updated Successfully";
        } else { // Create new record
            userProfileService.CreateUserProfile(city, country, gender, skills, cvFilePath, github, linkedin, profilePicturePath);
            message = "User Profile Created Successfully";
        }
        model.addAttribute("successMessage", message);
        //model.addAttribute("profile", profile);
        // Render the template (e.g., profile.html)
        return "user_profile";
    }
    @PostMapping(value = "/company_profile_create")
    public String createCompanyProfile(@ModelAttribute UserProfile profile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();

        //profile.(userId); // Always ensure user ID is set

        User user = userRepository.findById(userId).orElse(null);

        // Check if profile already exists for this user
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);

        String message;
        if (existingProfile.isPresent()) {
            UserProfile dbProfile = existingProfile.get();

            // copy submitted values into existing entity
            dbProfile.setCompanyName(profile.getCompanyName());
            dbProfile.setCompanyAddress(profile.getCompanyAddress());
            dbProfile.setCompanyEmail(profile.getCompanyEmail());
            dbProfile.setCompanyUrl(profile.getCompanyUrl());
            dbProfile.setCompanyPhone(profile.getCompanyPhone());
            dbProfile.setCompanyBusiness(profile.getCompanyBusiness());

            profile = userProfileRepository.save(dbProfile);
            message = "Company Profile Updated Successfully";
        }else{
            profile.setUser(user);
            userProfileRepository.save(profile);
            message = "Company Profile Created Successfully";
        }

        model.addAttribute("comMessage", message);
        model.addAttribute("profile", profile);
        return "user_profile";
    }
}