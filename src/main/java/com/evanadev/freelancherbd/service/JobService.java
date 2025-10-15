package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job JobSave(Job job) {
        return jobRepository.save(job);
    }

    public void update_job(Job job) {

        Job existing = jobRepository.findById(job.getId()).get();
        existing.setTitle(job.getTitle());
        existing.setDescription(job.getDescription());
        existing.setCategory(job.getCategory());
        existing.setJobType(job.getJobType());
        existing.setPaymentAmount(job.getPaymentAmount());
        existing.setRequiredSkill(job.getRequiredSkill());
        existing.setRequiredLevel(job.getRequiredLevel());
        existing.setJobStatus(job.getJobStatus());
        existing.setDeadline(job.getDeadline());
        existing.setAgeLimit(job.getAgeLimit());
        existing.setVacancy(job.getVacancy());
        existing.setExperience(job.getExperience());
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

    public List<Job> findByJobType(JobType jobType) {
        return jobRepository.findByJobType(jobType);
    }

    public List<Job> findByJobStatus(User loggedInUser, JobStatus jobStatus) {
        return jobRepository.findByJobStatus(loggedInUser.getUsername(), jobStatus);
    }
}
