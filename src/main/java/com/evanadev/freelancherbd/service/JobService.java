package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.controller.JobController;
import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Job JobSave(Job job) {

        return jobRepository.save(job);
    }

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

        Job existing = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        existing.setJobStatus(status);
        existing.setPostedAt(LocalDate.now());
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

    public List<Job> findByNewJobStatus(JobStatus jobStatus) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        return jobRepository.findByNewJobStatus(jobStatus,startOfWeek,endOfWeek);
    }

    public List<Job> findByRecommendedJobStatus(JobStatus jobStatus, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // split user skills (e.g., "Java, Spring, AWS")
        List<String> userSkills = Arrays.stream(
                user.getUserProfile().getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        // get all active jobs
        List<Job> jobs = jobRepository.findByRecommendedJobStatus(jobStatus);

        // filter jobs that contain any of the user's skills
        return jobs.stream()
                .filter(j -> j.getRequiredSkill() != null)
                .filter(j -> userSkills.stream().anyMatch(
                        skill -> j.getRequiredSkill().toLowerCase().contains(skill)))
                .toList();

    }
    public List<Job> findBySavedJobs(TrafficType jobStatus, Long userId) {
        return jobRepository.findByTrackingJobStatus(jobStatus, userId);
    }

    public List<Job> findByPendingPaymentJobs(Long userId) {
        return jobRepository.findByJobTrackingCompleted(userId);
    }


}
