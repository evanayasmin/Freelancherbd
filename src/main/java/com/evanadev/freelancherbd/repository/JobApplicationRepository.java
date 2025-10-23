package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.ApplicationStatus;
import com.evanadev.freelancherbd.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    //boolean existsByJobApplication(JobApplication jobApplication);
    boolean findById(long id);
    Optional<JobApplication> findById(Long id);
    JobApplication findJobApplicationById(long id);
    Optional<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findJobApplicationByUserId(long userId);
    List<JobApplication> findJobApplicationByJobId(Long jobId);

    @Query("SELECT DISTINCT ja FROM JobApplication ja JOIN ja.job j WHERE j.user.id = :employerId " +
            "AND ja.applicationStatus IN ('RECEIVED', 'VIEWED')")
    List<JobApplication> findJobApplicationByEmployerId(long employerId);

    @Query("SELECT DISTINCT ja FROM JobApplication ja JOIN ja.job j WHERE j.user.id = :employerId " +
            "AND ja.applicationStatus IN ('SORTED')")
    List<JobApplication> findSortlistedApplicationByEmployerId(long employerId);

    @Query("SELECT DISTINCT ja FROM JobApplication ja JOIN ja.job j WHERE j.user.id = :employerId " +
            "AND ja.applicationStatus IN ('HIRED')")
    List<JobApplication> findHiredApplicationByEmployerId(long employerId);

    @Query("SELECT DISTINCT ja FROM JobApplication ja JOIN ja.job j WHERE j.user.id = :employerId " +
            "AND ja.applicationStatus IN ('DECLINED')")
    List<JobApplication> findDeclinedApplicationByEmployerId(long employerId);

    @Query("SELECT DISTINCT ja FROM JobApplication ja JOIN ja.job j WHERE j.user.id = :employerId " +
            "AND ja.applicationStatus IN ('PAID')")
    List<JobApplication> findPaidApplicationByEmployerId(long employerId);

}
