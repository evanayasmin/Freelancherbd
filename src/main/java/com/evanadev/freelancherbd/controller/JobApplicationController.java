package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.JobApplication;
import com.evanadev.freelancherbd.repository.JobApplicationRepository;
import com.evanadev.freelancherbd.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class JobApplicationController {
    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    JobApplicationService jobApplicationService;

   public JobApplicationController(JobApplicationRepository jobApplicationRepository, JobApplicationService jobApplicationService){
       this.jobApplicationRepository = jobApplicationRepository;
        this.jobApplicationService = jobApplicationService;
   }

}
