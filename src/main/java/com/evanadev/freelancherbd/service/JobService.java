package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Job;
import com.evanadev.freelancherbd.model.JobStatus;
import com.evanadev.freelancherbd.model.JobType;
import com.evanadev.freelancherbd.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job save(Job job) {
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
        jobRepository.save(existing);

    }
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<Job> findByCategory(Category category) {
        return jobRepository.findByCategory(category);
    }

    public List<Job> findByJobType(JobType jobType) {
        return jobRepository.findByJobType(jobType);
    }

    public List<Job> findByJobStatus(JobStatus jobStatus) {
        return jobRepository.findByJobStatus(jobStatus);
    }
}
