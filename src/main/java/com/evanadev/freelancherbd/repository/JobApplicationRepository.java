package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.ApplicationStatus;
import com.evanadev.freelancherbd.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    //boolean existsByJobApplication(JobApplication jobApplication);
    boolean findById(long id);
    Optional<JobApplication> findById(Long id);

    Optional<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findJobApplicationByUserId(long userId);
    List<JobApplication> findJobApplicationByJobId(Long jobId);

}
