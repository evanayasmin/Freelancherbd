package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobReportRepository extends JpaRepository<JobReport, Long> {

    @Query("""
    SELECT DISTINCT jr
    FROM JobReport jr
    JOIN FETCH jr.job jb
    JOIN FETCH jb.category
    WHERE jr.user.id = :userId
    """)
    List<JobReport> findByReportJob(@Param("userId") Long userId);
}
