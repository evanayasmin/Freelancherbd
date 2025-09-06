package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public void RegisterUser(String fullname, String username, String email, String password,String phone, String role, Integer status, Long nid)
    {
    User user = new User();
    user.setUsername(username);
    user.setFullname(fullname);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setPhone(phone);
    user.setRole(role);
    user.setStatus(1);
    user.setNid(nid);
    userRepository.save(user);
    }

}
