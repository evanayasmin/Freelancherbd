package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.EarningReportRowDTO;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.service.FreelancerEarningService;
import com.evanadev.freelancherbd.service.WalletService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/freelancer/earnings")
public class EarningController {
    private final FreelancerEarningService earningService;
    private final WalletService walletService;

    public EarningController(FreelancerEarningService earningService, WalletService walletService) {
        this.earningService = earningService;
        this.walletService = walletService;
    }

    /**
     * Earnings report page
     * Shows:
     *  - Wallet balance
     *  - Total paid earnings
     *  - Job-wise paid earnings
     */

    @PreAuthorize("hasRole('FREELANCER')")
    @GetMapping
    public String viewEarnings(Authentication authentication, Model model) {

        Long freelancerId = extractFreelancerId(authentication);

        // Paid earnings report
        List<EarningReportRowDTO> earnings =
                earningService.getPaidEarnings(freelancerId);

        BigDecimal totalEarnings =
                earningService.getTotalPaidEarnings(freelancerId);

        BigDecimal walletBalance =
                walletService.getWalletBalance(freelancerId);

        model.addAttribute("earnings", earnings);
        model.addAttribute("totalEarnings", totalEarnings);
        model.addAttribute("wallet", walletBalance);

        return "freelancer/earnings/report";
    }

    /**
     * Extract freelancer id from authenticated principal
     * This keeps controller clean and testable
     */
    private Long extractFreelancerId(Authentication authentication) {

        // Example:
        // CustomUserDetails implements UserDetails
        // and exposes getUserId()

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetail userDetails) {
            return userDetails.getId();
        }

        throw new IllegalStateException("Authenticated user is not a freelancer");
    }
}
