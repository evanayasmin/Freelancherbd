package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.JobTraffic;
import com.evanadev.freelancherbd.model.TrafficType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobTrafficRepository extends JpaRepository<JobTraffic, Long> {
    JobTraffic findJobTrafficByJobId(Long jobId);


    @Query("""
    SELECT DISTINCT t
    FROM JobTraffic t
    WHERE t.user.id = :userId 
      AND t.job.id = :jobId 
      AND t.trafficType = :trafficType
""")
    Optional<JobTraffic> findByUserIdJobId(
            @Param("userId") Long userId,
            @Param("jobId") Long jobId,
            @Param("trafficType") TrafficType trafficType
    );

    @Query("""
    SELECT DISTINCT t
    FROM JobTraffic t
    WHERE t.user.id = :userId 
      AND t.job.id = :jobId 
    """)
    List<JobTraffic> findJobTrafficByUserIdJobId(
            @Param("userId") Long userId,
            @Param("jobId") Long jobId
    );

    /**
     * Job list for Employer based on trafficType status
     */

    @Query("""
    SELECT DISTINCT t
    FROM JobTraffic t JOIN Job jb ON t.job.id = jb.id 
    WHERE t.trafficType =:trafficType AND jb.user.id = :userId
    """)
    List<JobTraffic> findJobTrafficByStatus(
            @Param("userId") Long userId,
            @Param("trafficType") TrafficType trafficType
    );
}
