package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.dto.EarningReportRowDTO;
import com.evanadev.freelancherbd.repository.FreelancerEarningRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FreelancerEarningService {

    private final FreelancerEarningRepository repository;

    public FreelancerEarningService(FreelancerEarningRepository repository) {
        this.repository = repository;
    }

    public List<EarningReportRowDTO> getPaidEarnings(Long freelancerId) {
        return repository.findEarnings(freelancerId);
    }

    public BigDecimal getTotalPaidEarnings(Long freelancerId) {
        return repository.totalEarnings(freelancerId);
    }
}
