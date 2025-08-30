package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public void RegisterUser(String fname, String lname, String username, String email, String password, String country, String city,
                             String address, String phone, String role, String gender, Integer status, Date birthday)
    {
    User user = new User();
    user.setUsername(username);
    user.setFname(fname);
    user.setLname(lname);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setCountry(country);
    user.setCity(city);
    user.setAddress(address);
    user.setPhone(phone);
    user.setRole("user");
    user.setGender(gender);
    user.setStatus(1);
    user.setGender(gender);
    user.setBirthdate(birthday);
    userRepository.save(user);
    }

}
