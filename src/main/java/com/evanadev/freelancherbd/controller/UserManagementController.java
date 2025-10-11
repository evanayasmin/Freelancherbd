package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Status;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserStatus;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserManagementController {
    @Autowired
    private UserService userService;
    @Autowired private AESUtil aesUtil;
    private static final Logger log = LoggerFactory.getLogger(UserManagementController.class);

    @GetMapping("/admin/users/freelancer_list")
    public String allFreelancers(Model model) {

        List<User> freelancers = userService.GetAllFreelancers();
        model.addAttribute("freelancers", freelancers);
        model.addAttribute("statuses", UserStatus.values());
        return "freelancer_list";
    }

    @GetMapping("/admin/users/client_list")
    public String allClients(Model model) {

        List<User> clients = userService.GetAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("statuses", UserStatus.values());
        return "client_list";
    }
    @GetMapping("/admin/users/profile_details")
    public String userDetail(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            User user = userService.findUserDetailById(did)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            log.debug("User details: {}", user);
            if (user.getUserProfile() != null) {
                user.getUserProfile().getId(); // touch a field to initialize
            }
            model.addAttribute("userDetail", user);
            model.addAttribute("statuses", UserStatus.values());
        }
        return "fragments/user_detail :: profileDetail";
    }

    @PostMapping("/admin/users/statusUpdate")
    @ResponseBody
    public ResponseEntity<String>UserStatusUpdate(@RequestParam String encId, @RequestParam String status)
    {
        if(encId != null) {
            Long did = aesUtil.decryptId(encId);
            userService.UpdateUserStatus(did, status);
            return ResponseEntity.ok("success");
        }else
            return ResponseEntity.ok("failed");


    }

}
