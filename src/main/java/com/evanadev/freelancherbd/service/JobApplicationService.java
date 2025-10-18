package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.JobApplication;
import com.evanadev.freelancherbd.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public JobApplicationService (JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public JobApplication Application_submit(JobApplication application) {
        JobApplication savedApplication = jobApplicationRepository.save(application);
        return savedApplication;
    }

    public void update_application(JobApplication application) {

        JobApplication existing = jobApplicationRepository.findById(application.getId()).get();
        existing.setApplicationStaus(application.getApplicationStaus());
        existing.setJobAssignedDate(application.getJobAssignedDate());
        jobApplicationRepository.save(application);

    }
    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id).get();
    }

    public List<JobApplication> findJobApplicationByJobId(Long jobId) {
        return jobApplicationRepository.findJobApplicationByJobId(jobId);
    }

    public List<JobApplication> findJobApplicationByUserId(Long employeeId) {
        return jobApplicationRepository.findJobApplicationByUserId(employeeId);
    }

}
