package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Payment;
import com.evanadev.freelancherbd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByEmployerId(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
      AND p.status = 'COMPLETED'
""")
    BigDecimal getTotalPaidAmount(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
      
""")
    BigDecimal getTotalTransactionAmount(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
    AND p.status = 'PENDING'
      
""")
    BigDecimal getTotalPendingAmount(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
    AND p.status = 'PROCESSING'
      
""")
    BigDecimal getTotalProcessAmount(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
    AND p.status = 'FAILD'
      
""")
    BigDecimal getTotalFailedAmount(User employer);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.employerId = :employer
    AND p.status = 'REFUNDED'
      
""")
    BigDecimal getTotalRefundAmount(User employer);

}
