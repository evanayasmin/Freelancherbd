package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.JobReport;
import com.evanadev.freelancherbd.repository.JobReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobReportService {
    @Autowired
    private JobReportRepository jobReportRepository;

    public JobReport findById(long id) {
        return jobReportRepository.findById(id).orElse(null);
    }
    public List<JobReport> findAll() {
        return jobReportRepository.findAll();
    }
    public JobReport save(JobReport jobReport) {
        return jobReportRepository.save(jobReport);
    }
    public void deleteById(long id) {
        jobReportRepository.deleteById(id);
    }

    public List<JobReport> findByReportJob( Long userId) {
        return jobReportRepository.findByReportJob(userId);
    }

}
