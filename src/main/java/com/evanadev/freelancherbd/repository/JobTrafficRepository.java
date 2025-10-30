package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.JobTraffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTrafficRepository extends JpaRepository<JobTraffic, Long> {
    JobTraffic findJobTrafficByJobId(Long jobId);
    boolean existsJobTrafficByJobId(Long jobId);
}
