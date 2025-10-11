package com.evanadev.freelancherbd.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import java.time.LocalDateTime;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String RequiredSkill;
    private String RequiredLevel;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    private String PaymentAmount;

    private LocalDateTime PaymentDate;
    private String PaymentMethod;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime CreatedAt;

    @LastModifiedDate
    private LocalDateTime CompletedAt;

    @LastModifiedBy
    @Column(updatable = false)
    private String CompletedBy;

    private LocalDateTime CancelledAt;

    @CreatedBy
    @Column(updatable = false)
    private String CreatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRequiredSkill() {
        return RequiredSkill;
    }

    public void setRequiredSkill(String requiredSkill) {
        RequiredSkill = requiredSkill;
    }

    public String getRequiredLevel() {
        return RequiredLevel;
    }

    public void setRequiredLevel(String requiredLevel) {
        RequiredLevel = requiredLevel;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        jobType = jobType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        jobStatus = jobStatus;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public LocalDateTime getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCancelledAt() {
        return CancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        CancelledAt = cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return CompletedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        CompletedAt = completedAt;
    }

    public String getCompletedBy() {
        return CompletedBy;
    }

    public void setCompletedBy(String completedBy) {
        CompletedBy = completedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }
}
