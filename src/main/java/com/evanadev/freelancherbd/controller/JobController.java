package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobService jobService;

    private CategoryService categoryService;

    @Autowired
    private AESUtil aesUtil;

    public JobController(JobRepository jobRepository, JobService jobService, CategoryService categoryService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.categoryService = categoryService;
    }

    @GetMapping ("/employer/jobs/create_job")
    public String CreateJob(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("job", new Job());
        return "job_form";
    }

    //Save new Category
    @PostMapping("/employer/job/submit_job")
    public String job_submit(@ModelAttribute Job job, Model model){

        String message = "";
        jobService.JobSave(job);
        message = "Your Job is created successfully. After Admin approval, it will be posted for Freelancer.";
        model.addAttribute("messsage", message);
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("job", new Job());
        return "job_form";
    }

    @GetMapping("/employer/jobs/job_list")
    public String allJobList(@ModelAttribute("loggedUser") CustomUserDetail loggedUser, Model model) {
        log.info("Loggedin user=",loggedUser.getUser().getUsername());
        List<Job> jobs = jobService.findAllJobsWithCategory(loggedUser.getUser());
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("aesUtil", aesUtil);
        return "alljob_list";

    }

    @GetMapping("/employer/jobs/job_detail")
    public String jobDetail(@RequestParam("encId") String encId, Model model) {
        if (encId != null) {
            Long did = aesUtil.decryptId(encId);
            Job job  = jobService.findById(did);
            log.debug("Job details: {}", job);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("jobDetail", job);
            model.addAttribute("jobStatus", JobStatus.values());
            model.addAttribute("jobType", JobType.values());
            model.addAttribute("categories", categories);
        }else{
            model.addAttribute("message", "Job not available.");
        }

        return "fragments/job_detail :: jobDetail";
    }

    @PostMapping("/employer/jobs/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> UserStatusUpdate(@ModelAttribute Job job)
    {
        Map<String, String> response = new HashMap<>();

        Optional<Job> existingjob = jobRepository.findById(job.getId());
            if (existingjob.isPresent()){
                jobService.update_job(job);
                response.put("status", "success");
                return ResponseEntity.ok(response);
            }

        response.put("status", "failed");
        return ResponseEntity.ok(response);
    }

}
