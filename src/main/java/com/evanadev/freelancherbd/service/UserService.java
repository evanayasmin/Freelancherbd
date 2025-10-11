package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Role;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserStatus;
import com.evanadev.freelancherbd.repository.RoleRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    user.setStatus(UserStatus.ACTIVE);
    user.setNid(nid);
    userRepository.save(user);
    }

    public List<User> GetAllFreelancers(){
        return userRepository.findAllFreelancers();
    }

    public List<User> GetAllClients(){
        return userRepository.findAllClients();
    }

    public Optional<User>findUserDetailById(Long id){
        return userRepository.findUserDetails(id);
    }

    public void UpdateUserStatus(Long id,String status){
        User existing = userRepository.findById(id).get();
        existing.setStatus(UserStatus.valueOf(status));
        userRepository.save(existing);
    }

}
