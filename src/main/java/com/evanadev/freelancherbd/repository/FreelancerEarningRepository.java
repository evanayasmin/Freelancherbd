package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.dto.EarningReportRowDTO;
import com.evanadev.freelancherbd.model.FreelancerEarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface FreelancerEarningRepository extends JpaRepository<FreelancerEarning, Long> {

    @Query("""
        SELECT new com.evanadev.freelancherbd.dto.EarningReportRowDTO(
            j.title,
            e.grossAmount,
            e.platformFee,
            e.netAmount,
            e.paidAt
        )
        FROM FreelancerEarning e
        LEFT JOIN e.job j
        WHERE e.freelancer.id = :freelancerId
        ORDER BY e.paidAt DESC
    """)
    List<EarningReportRowDTO> findEarnings(Long freelancerId);

    @Query("""
        SELECT COALESCE(SUM(e.netAmount), 0)
        FROM FreelancerEarning e
        WHERE e.freelancer.id = :freelancerId
    """)
    BigDecimal totalEarnings(Long freelancerId);

}
