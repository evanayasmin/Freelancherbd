package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Job;
import com.evanadev.freelancherbd.model.JobStatus;
import com.evanadev.freelancherbd.model.TrafficType;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.JobRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FreelancerService {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;

    private UserRepository userRepository;

    @Autowired
    public FreelancerService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Map<Job, List<User>> findByRecommendedFreelancer(Long userId) {

        // Get all active jobs
        List<Job> jobs = jobRepository.findByRecommendedFreelancer(userId);

        // Get all freelancers
        List<User> freelancers = userRepository.findAllFreelancers();

        // Map to store job -> list of matching freelancers
        Map<Job, List<User>> recommendedMap = new HashMap<>();

        for (Job job : jobs) {
            if (job.getRequiredSkill() == null || job.getRequiredSkill().isEmpty()) continue;

            String jobSkills = job.getRequiredSkill().toLowerCase();

            // Find matching freelancers
            List<User> matchedFreelancers = freelancers.stream()
                    .filter(u -> u.getUserProfile() != null && u.getUserProfile().getSkills() != null)
                    .filter(u -> {
                        List<String> freelancerSkills = Arrays.stream(
                                        u.getUserProfile().getSkills().split(","))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .toList();

                        // If any skill matches job skill
                        return freelancerSkills.stream().anyMatch(jobSkills::contains);
                    })
                    .toList();

            recommendedMap.put(job, matchedFreelancers);
        }

        return recommendedMap;

    }

    public List<User> searchFreelancers(String skill_title, String title, String jobType, String required_level){

       return userRepository.searchFreelancers(skill_title, title, jobType, required_level);
    }



}
