package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByFreelancerId(Long freelancerId);
}

