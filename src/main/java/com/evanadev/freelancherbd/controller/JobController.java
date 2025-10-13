package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.service.CategoryService;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class JobController {
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
       //model.addAttribute("aesUtil", aesUtil);
        model.addAttribute("job", new Job());
        return "job_form";
    }

}
