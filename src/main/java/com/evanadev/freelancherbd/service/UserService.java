package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Role;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.RoleRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository=userRepository;
        this.roleRepository = roleRepository;
    }

    public void RegisterUser(String fullname, String username, String email, String password,String phone, String roleName, Long nid)
    {
    User user = new User();
    user.setUsername(username);
    user.setFullname(fullname);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setPhone(phone);

    Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

    // Assign role to user
    user.setRoles(Set.of(role));
    user.setStatus(1);
    user.setNid(nid);
    userRepository.save(user);
    }

}
