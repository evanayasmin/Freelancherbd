package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.Role;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import com.evanadev.freelancherbd.repository.UserProfileRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService (UserProfileRepository userProfileRepository, UserRepository userRepository) {
      this.userProfileRepository = userProfileRepository;
      this.userRepository = userRepository;
    }

    public UserProfile CreateUserProfile(String city, String country, String gender, String skill, String cv, String
                                  github, String linkedin, String company_name, String company_address, String company_email,
                                  String company_business, String company_phone, String company_url, String profile_picture) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomUserDetail) authentication.getPrincipal()).getId();

        // 1. Fetch User by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setCity(city);
        userProfile.setCountry(country);
        userProfile.setGender(gender);
        userProfile.setCv(cv);
        userProfile.setSkills(skill);
        userProfile.setGithubUrl(github);
        userProfile.setLinkedinUrl(linkedin);
        userProfile.setCompanyName(company_name);
        userProfile.setCompanyAddress(company_address);
        userProfile.setCompanyEmail(company_email);
        userProfile.setCompanyBusiness(company_business);
        userProfile.setCompanyPhone(company_phone);
        userProfile.setCompanyUrl(company_url);
        userProfile.setProfilePicture(profile_picture);
        return userProfileRepository.save(userProfile);
    }

}
