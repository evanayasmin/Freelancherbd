package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.JobTraffic;
import com.evanadev.freelancherbd.repository.JobTrafficRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
