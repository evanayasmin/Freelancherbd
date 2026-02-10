package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Wallet;
import com.evanadev.freelancherbd.model.WithdrawalRequest;
import com.evanadev.freelancherbd.model.WithdrawalStatus;
import com.evanadev.freelancherbd.repository.WalletRepository;
import com.evanadev.freelancherbd.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    public Wallet getWallet(Long freelancerId) {
        return walletRepository.findByFreelancerId(freelancerId);
    }

    public void requestWithdrawal(WithdrawalRequest request) {
        request.setStatus(WithdrawalStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        withdrawalRepository.save(request);
    }
}
