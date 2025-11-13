package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.service.FreelancerService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.service.UserService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class FreelancerController {

    @Autowired
    private UserService userService;

    private JobService jobService;
    private AESUtil aesUtil;

    private static final Logger log = LoggerFactory.getLogger(FreelancerController.class);
    @Autowired
    private FreelancerService freelancerService;

    public FreelancerController(UserService userService, JobService jobService, AESUtil aesUtil ) {
        this.userService = userService;
        this.jobService = jobService;
        this.aesUtil = aesUtil;
    }


    @GetMapping("/skilled_freelancer")
    public String GetSkilledFreelancer(Model model){

        return "skilled_freelancer";
    }

    @GetMapping("/toprated_freelancer")
    public String GetTopFreelancer(Model model){

        return "top_rated_freelancer";
    }

    @GetMapping("/employer/recommended_freelancer")
    public String GetRecommendedFreelancer(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model){

        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        Long userId = loggedUser.getId();
        Map<Job, List<User>> recommendedMap =
                freelancerService.findByRecommendedFreelancer(userId);

        // Flatten all recommended freelancers (avoid duplicates)
        List<User> recommendedFreelancers = recommendedMap.values().stream()
                .flatMap(List::stream)
                .distinct()
                .toList();

        model.addAttribute("freelancers", recommendedFreelancers);
        model.addAttribute("aesUtil", aesUtil);
        return "recommended_freelancers";
    }

    @GetMapping("/freelancer/profile_detail/{encId}")
    public String getJobDetail(@PathVariable("encId") String encId, Model model) {
        try {
            Long did = aesUtil.decryptId(encId);
            User user = userService.findByUserId(did);

            log.info("profileDetails: {}", user.toString());

            model.addAttribute("profile", user);
            return "freelancer_detail";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:error";
        }
    }

}
