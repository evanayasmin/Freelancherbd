package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.JobApplication;
import com.evanadev.freelancherbd.repository.JobApplicationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        JobApplication existing = jobApplicationRepository.findJobApplicationById(application.getId());
        existing.setApplicationStatus(application.getApplicationStatus());
        existing.setApprovedSalary(application.getApprovedSalary());
        existing.setJobAssignedDate(LocalDateTime.now());
        jobApplicationRepository.save(existing);

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

    public List<JobApplication> findJobApplicationByEmployerId(Long employerId) {
        return jobApplicationRepository.findJobApplicationByEmployerId(employerId);
    }

    public List<JobApplication> findSortlistedApplicationByEmployerId(Long employerId) {
        return jobApplicationRepository.findSortlistedApplicationByEmployerId(employerId);
    }

    public List<JobApplication> findHiredApplicationByEmployerId(Long employerId) {
        return jobApplicationRepository.findHiredApplicationByEmployerId(employerId);
    }

    public List<JobApplication> findDeclinedApplicationByEmployerId(Long employerId) {
        return jobApplicationRepository.findDeclinedApplicationByEmployerId(employerId);
    }

    public List<JobApplication> findPaidApplicationByEmployerId(Long employerId) {
        return jobApplicationRepository.findPaidApplicationByEmployerId(employerId);

    }


}
