package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.Job;
import com.evanadev.freelancherbd.model.JobStatus;
import com.evanadev.freelancherbd.model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findById(Long id);
    List<Job> findByJobStatus(JobStatus jobStatus);
    List<Job> findByJobType(JobType jobType);
    List<Job> findByCategory(Category category);
}
