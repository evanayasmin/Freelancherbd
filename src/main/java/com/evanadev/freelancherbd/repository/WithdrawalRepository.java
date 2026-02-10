package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.WithdrawalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRepository extends JpaRepository<WithdrawalRequest, Long> {

}
