package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.JobTraffic;
import com.evanadev.freelancherbd.model.TrafficType;
import com.evanadev.freelancherbd.repository.JobTrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobTrafficService {
    @Autowired
    private JobTrafficRepository jobTrafficRepository;

    public JobTraffic save(JobTraffic jobTraffic){
        return jobTrafficRepository.save(jobTraffic);
    }
    public JobTraffic update(JobTraffic jobTraffic){
        return jobTrafficRepository.save(jobTraffic);
    }
    public void deleteById(Long id){
        jobTrafficRepository.deleteById(id);
    }
    public List<JobTraffic> findAll(){
        return jobTrafficRepository.findAll();
    }

    public Optional<JobTraffic> findByUserIdJobId(Long userId, Long jobId, TrafficType trafficType){
        return jobTrafficRepository.findByUserIdJobId(userId, jobId, trafficType);
    }

    public List<JobTraffic> findJobTrafficByUserIdJobId(Long userId, Long jobId){
        return jobTrafficRepository.findJobTrafficByUserIdJobId(userId, jobId);
    }

    public List<JobTraffic> findJobTrafficByStatus(Long userId, TrafficType trafficType){
        return jobTrafficRepository.findJobTrafficByStatus(userId, trafficType);
    }
}
