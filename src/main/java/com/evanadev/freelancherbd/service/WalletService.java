package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.*;
import com.evanadev.freelancherbd.repository.WalletRepository;
import com.evanadev.freelancherbd.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        Wallet wallet = walletRepository.findByFreelancerId(request.getFreelancer().getId());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        request.setStatus(WithdrawalStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        WithdrawalRequest newRequest = new WithdrawalRequest();
        newRequest.setAmount(request.getAmount());
        newRequest.setMethod(request.getMethod());
        newRequest.setAccountNumber(request.getAccountNumber());
        newRequest.setStatus(WithdrawalStatus.PENDING);
        newRequest.setRequestDate(LocalDateTime.now());
        withdrawalRepository.save(request);
    }

    public void approveWithdraw(Long requestId) {
        WithdrawalRequest req = withdrawalRepository.findById(requestId).orElseThrow();
        Wallet wallet = walletRepository.findByFreelancerId(req.getFreelancer().getId());

        wallet.setBalance(wallet.getBalance().subtract(req.getAmount()));
        req.setStatus(WithdrawalStatus.APPROVED);

        walletRepository.save(wallet);
        withdrawalRepository.save(req);
    }

    public BigDecimal getWalletBalance(Long freelancerId){

        BigDecimal walletBalance = walletRepository.getWalletBalance(freelancerId);

        return walletBalance;
    }

}
