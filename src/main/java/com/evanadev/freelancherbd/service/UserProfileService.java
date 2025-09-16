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

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService (UserProfileRepository userProfileRepository, UserRepository userRepository) {
      this.userProfileRepository = userProfileRepository;
      this.userRepository = userRepository;
    }

    public UserProfile CreateUserProfile(String city, String country, String gender, String skill, String cv, String github, String linkedin, String profile_picture) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomUserDetail) authentication.getPrincipal()).getId();

        //Fetch User by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        UserProfile savedProfile = null;
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            //userProfile.setUser(user);
            userProfile.setCity(city);
            userProfile.setCountry(country);
            userProfile.setGender(gender);
            userProfile.setCv(cv);
            userProfile.setSkills(skill);
            userProfile.setGithubUrl(github);
            userProfile.setLinkedinUrl(linkedin);
            userProfile.setProfilePicture(profile_picture);
            savedProfile = userProfileRepository.save(userProfile);
        }
        else{
            UserProfile Profile = new UserProfile();
            Profile.setUser(user);
            Profile.setCity(city);
            Profile.setCountry(country);
            Profile.setGender(gender);
            Profile.setCv(cv);
            Profile.setSkills(skill);
            Profile.setGithubUrl(github);
            Profile.setLinkedinUrl(linkedin);
            Profile.setProfilePicture(profile_picture);
            savedProfile = userProfileRepository.save(Profile);
        }
        return savedProfile;
    }

    public UserProfile CreateCompanyProfile(String company_name, String company_email, String company_address, String company_phone, String company_business, String company_url){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomUserDetail) authentication.getPrincipal()).getId();

        //Fetch User by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        UserProfile savedProfile = null;
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            UserProfile userProfile = existingProfile.get();
            userProfile.setCompanyName(company_name);
            userProfile.setCompanyEmail(company_email);
            userProfile.setCompanyAddress(company_address);
            userProfile.setCompanyBusiness(company_business);
            userProfile.setCompanyPhone(company_phone);
            userProfile.setCompanyUrl(company_url);
            savedProfile = userProfileRepository.save(userProfile);
        }else {
            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user);
            userProfile.setCompanyName(company_name);
            userProfile.setCompanyEmail(company_email);
            userProfile.setCompanyAddress(company_address);
            userProfile.setCompanyBusiness(company_business);
            userProfile.setCompanyPhone(company_phone);
            userProfile.setCompanyUrl(company_url);
            savedProfile = userProfileRepository.save(userProfile);
        }
        return savedProfile;

    }

}
