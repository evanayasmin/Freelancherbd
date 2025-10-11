package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.Job;
import com.evanadev.freelancherbd.repository.CategoryRepository;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.service.JobService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobController {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobService jobService;

    @Autowired
    private AESUtil aesUtil;

    public JobController(JobRepository jobRepository, JobService jobService, CategoryRepository categoryRepository) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @GetMapping ("/employer/jobs/create_job")
    public String CreateJob(Model model) {

        model.addAttribute("job", new Job());
        return "job_form";
    }

}
