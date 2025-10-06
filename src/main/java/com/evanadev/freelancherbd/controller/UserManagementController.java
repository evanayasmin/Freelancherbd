package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserManagementController {

    @Autowired
    private UserService userService;


    @GetMapping("/admin/users/freelancer_list")
    public String allFreelancers(Model model) {

        List<User> freelancers = userService.GetAllFreelancers();
        model.addAttribute("freelancers", freelancers);

        return "freelancer_list";
    }

    @GetMapping("/admin/users/client_list")
    public String allClients(Model model) {

        List<User> clients = userService.GetAllClients();
        model.addAttribute("clients", clients);

        return "client_list";
    }

}
