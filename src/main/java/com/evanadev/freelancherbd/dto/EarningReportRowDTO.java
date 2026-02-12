package com.evanadev.freelancherbd.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EarningReportRowDTO {

    private String jobTitle;
    private BigDecimal grossAmount;
    private BigDecimal platformFee;
    private BigDecimal netAmount;
    private LocalDateTime paidAt;

    //Required for JPQL constructor projection
    public EarningReportRowDTO(
            String jobTitle,
            BigDecimal grossAmount,
            BigDecimal platformFee,
            BigDecimal netAmount,
            LocalDateTime paidAt) {

        this.jobTitle = jobTitle;
        this.grossAmount = grossAmount;
        this.platformFee = platformFee;
        this.netAmount = netAmount;
        this.paidAt = paidAt;
    }

    // Getters (NO setters – immutable report DTO)

    public String getJobTitle() {
        return jobTitle;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
