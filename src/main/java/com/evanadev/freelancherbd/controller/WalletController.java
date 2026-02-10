package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.Wallet;
import com.evanadev.freelancherbd.model.WithdrawalRequest;
import com.evanadev.freelancherbd.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/freelancer/wallet")
public class WalletController {

    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public String walletBalance(Model model, @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail) {
        Long freelancerId = loggedUserDetail.getId(); // get from logged-in user
        Wallet wallet = walletService.getWallet(freelancerId);
        model.addAttribute("wallet", wallet);
        return "wallet-balance";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(Model model) {
        model.addAttribute("withdrawal", new WithdrawalRequest());
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String submitWithdraw(@ModelAttribute WithdrawalRequest withdrawal, @ModelAttribute("loggedUser") CustomUserDetail loggedUserDetail) {
        withdrawal.setFreelancer(loggedUserDetail.getUser());
        walletService.requestWithdrawal(withdrawal);
        return "redirect:/freelancer/wallet/balance";
    }

}
