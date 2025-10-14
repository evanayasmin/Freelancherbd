package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findById(Long id);
    List<Job> findByJobStatus(JobStatus jobStatus);
    List<Job> findByJobType(JobType jobType);
    List<Job> findByCategory(Category category);

    @Query("SELECT DISTINCT j FROM Job j JOIN FETCH j.category WHERE j.CreatedBy = :createdBy")
    List<Job> findAllJobsWithCategory(@Param("createdBy") String createdBy);
}
