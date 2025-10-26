package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.controller.JobController;
import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job JobSave(Job job) {

        return jobRepository.save(job);
    }
    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    public void update_job(Job job, JobStatus jobStatus, JobType jobType) {

        Job existing = jobRepository.findById(job.getId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        log.info("Before update: Status={}, Type={}", existing.getJobStatus(), existing.getJobType());
        existing.setTitle(job.getTitle());
        existing.setDescription(job.getDescription());

        existing.setPaymentAmount(job.getPaymentAmount());
        existing.setRequiredSkill(job.getRequiredSkill());
        existing.setRequiredLevel(job.getRequiredLevel());

        existing.setDeadline(job.getDeadline());
        existing.setAgeLimit(job.getAgeLimit());
        existing.setVacancy(job.getVacancy());
        existing.setExperience(job.getExperience());
        existing.setCategory(job.getCategory());
        existing.setJobStatus(jobStatus);
        existing.setJobType(jobType);
        log.info("After setting: Status={}, Type={}", existing.getJobStatus(), existing.getJobType());
        jobRepository.save(existing);

    }

    public void updateJobStatus(Long jobId, JobStatus status) {
        System.out.println("Job Id="+ jobId);
        System.out.println("Job Status="+ status);
        Job existing = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        existing.setJobStatus(status);
        jobRepository.save(existing);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }
    public Job findById(Long id) {
        return jobRepository.findById(id).get();
    }

    public List<Job> findAllJobsWithCategory(User loggedInUser) {
        if (loggedInUser == null) {
            throw new IllegalArgumentException("Logged-in user cannot be null");
        }
        return jobRepository.findAllJobsWithCategory(loggedInUser.getUsername());
    }
    public List<Job> findByCategory(Category category) {
        return jobRepository.findByCategory(category);
    }

    public List<Job> findByCategoryAndJobStatus(Category category) {
        return jobRepository.findByCategoryAndJobStatus(category.getId());
    }

    public List<Job> findByJobType(JobType jobType) {
        return jobRepository.findByJobType(jobType);
    }

    public List<Job> findByJobStatus(User loggedInUser, JobStatus jobStatus) {
        return jobRepository.findByJobStatus(loggedInUser.getUsername(), jobStatus);
    }
    public List<Job> findByJobStatusForAdmin(JobStatus jobStatus) {
        return jobRepository.findByJobStatusForAdmin(jobStatus);
    }


}
