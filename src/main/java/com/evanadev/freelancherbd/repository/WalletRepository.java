package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByFreelancerId(Long freelancerId);

    /**
     * Calculate wallet balance for a freelancer
     * Wallet = Earnings + Admin Credits - Withdrawals
     */
    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN w.transactionType = 'CREDIT' THEN w.amount
                WHEN w.transactionType = 'DEBIT' THEN -w.amount
                ELSE 0
            END
        ), 0)
        FROM WalletTransaction w
        WHERE w.wallet.freelancer.id = :freelancerId
    """)
    BigDecimal getWalletBalance(@Param("freelancerId") Long freelancerId);
}

