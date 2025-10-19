package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobApplicationRepository;
import com.evanadev.freelancherbd.service.JobApplicationService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class JobApplicationController {
    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    JobApplicationService jobApplicationService;
    @Autowired
    JobService jobService ;
    @Autowired
    AESUtil aesUtil;
    private static final Logger log = LoggerFactory.getLogger(JobApplicationController.class);

   public JobApplicationController(JobApplicationRepository jobApplicationRepository, JobApplicationService jobApplicationService, JobService jobService, AESUtil aesUtil){
       this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicationService = jobApplicationService;
        this.jobService = jobService;
        this.aesUtil = aesUtil;
   }

    /*
     * @author: evana
     * @Desc: Application Form Details of a specific job
     * @Date: 18-10-25
     * */
    @GetMapping("/freelancer/jobs/application/{encId}")
    public String jobApplicationForm(@PathVariable String encId, @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Job job  = jobService.findById(did);
            User user = loggedUserDetail.getUser();
            log.info("User {} has been logged", user.getUserProfile().getCv());
            log.debug("Job details: {}", job);
            Optional<JobApplication> jobApplication = jobApplicationRepository.findByJobId(did);
            if (jobApplication.isPresent()) {
                model.addAttribute("application_message", "You have already applied for this post.");
            }else{
                model.addAttribute("application_message", "");
            }
            model.addAttribute("job", job);
            model.addAttribute("cv",user.getUserProfile().getCv());
            model.addAttribute("JobApplication", new JobApplication());
        }else{
            model.addAttribute("message", "Job not available.");
        }

        return "job_application_form";
    }

    /*
     * @author: evana
     * @Desc: Application Submit of a LoggedIn User as Employee
     * @Date: 18-10-25
     * */
    @PostMapping("/freelancer/jobs/application_submit")
    public String ApplyJob(@ModelAttribute JobApplication jobApplication, @RequestParam("jobId") Long jobId, @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail, Model model) {

        User user = loggedUserDetail.getUser();
        jobApplication.setUser(user);
        Job job = jobService.findById(jobId);
        jobApplication.setJob(job);

        jobApplication.setApplicationStaus(ApplicationStatus.RECEIVED);
        jobApplicationService.Application_submit(jobApplication);
        model.addAttribute("message", "Application submitted.");
        return "jobApplication_submit";
    }

    /*
     * @author: evana
     * @Desc: Proposals of LoggedIn User as Employer
     * @Date: 19-10-25
     * */

    @GetMapping("/employer/jobs/proposals")
    public String completeJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<JobApplication> jobApplication = jobApplicationService.findJobApplicationByEmployerId(loggedUser.getUser().getId());
        model.addAttribute("proposals", jobApplication);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        return "open_proposals";
    }


}
