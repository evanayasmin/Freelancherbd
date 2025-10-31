package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findById(Long id);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.jobStatus = :jobStatus and j.CreatedBy = :createdBy")
    List<Job> findByJobStatus(@Param("createdBy") String createdBy,  @Param("jobStatus") JobStatus jobStatus);

    List<Job> findByJobType(JobType jobType);
    List<Job> findByCategory(Category category);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category ct WHERE ct.id=:categoryId and j.jobStatus NOT in('PENDING', 'CANCELLED') ")
    List<Job> findByCategoryAndJobStatus(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.CreatedBy = :createdBy")
    List<Job> findAllJobsWithCategory(@Param("createdBy") String createdBy);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.jobStatus = :jobStatus")
    List<Job> findByJobStatusForAdmin(@Param("jobStatus") JobStatus jobStatus);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.jobStatus = :jobStatus AND j.PostedAt BETWEEN :startOfWeek AND :endOfWeek")
    List<Job> findByNewJobStatus(@Param("jobStatus") JobStatus jobStatus, @Param("startOfWeek") LocalDate startOfWeek,
                                 @Param("endOfWeek") LocalDate endOfWeek);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.jobStatus = :jobStatus")
    List<Job> findByRecommendedJobStatus(@Param("jobStatus") JobStatus jobStatus);

    @Query("SELECT DISTINCT jb FROM JobTraffic jt " +
            "JOIN jt.job jb " +
            "JOIN FETCH jb.category " +
            "WHERE jt.trafficType = :jobStatus AND jt.user.id = :userId")
    List<Job> findByTrackingJobStatus(@Param("jobStatus") TrafficType jobStatus, @Param("userId") Long userId);
}

