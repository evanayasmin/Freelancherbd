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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
            model.addAttribute("profile", null);
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
            userProfileRepository.save(profile);
            message = "User Profile Updated Successfully";
        } else { // Create new record
            userProfileService.CreateUserProfile(city, country, gender, skills, cvFilePath, github, linkedin, profilePicturePath);
            message = "User Profile Created Successfully";
        }
        model.addAttribute("successMessage", message);
        // Render the template (e.g., profile.html)
        return "user_profile";
    }
    @PostMapping(value = "/company_profile_create")
    public String createCompanyProfile(@RequestParam String company_name,
                                                @RequestParam String company_address,
                                                @RequestParam String company_email,
                                                @RequestParam String company_url,
                                                @RequestParam String company_business,
                                                @RequestParam String company_phone, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        String message = "";
        UserProfile profile;
        if (existingProfile.isPresent()) { // Update existing record
            profile = existingProfile.get();
            profile.setCompanyName(company_name);
            profile.setCompanyEmail(company_email);
            profile.setCompanyAddress(company_address);
            profile.setCompanyPhone(company_phone);
            profile.setCompanyBusiness(company_business);
            userProfileRepository.save(profile);
            message = "Company Profile Updated Successfully";
        } else { // Create new record
            userProfileService.CreateCompanyProfile(company_name, company_email, company_address, company_phone, company_business, company_url);
            message = "Company Profile Created Successfully";
        }
        model.addAttribute("comMessage", message);
        return "user_profile";
    }
}