package com.evanadev.freelancherbd.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    private Integer vacancy;
    private String ageLimit;
    private String experience;
    @Column(nullable = false)
    private String company;
    private String companyInstruction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "job")
    private List<JobTraffic> jobTraffic;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String RequiredSkill;
    private String RequiredLevel;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus = JobStatus.PENDING;

    private String PaymentAmount;

    @Column(nullable = false)
    private String jobLocation;

    private LocalDateTime PaymentDate;
    private String PaymentMethod;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime CreatedAt;

    private LocalDateTime CompletedAt;
    private String CompletedBy;

    private LocalDate PostedAt;
    private LocalDateTime CancelledAt;

    @CreatedBy
    @Column(updatable = false)
    private String CreatedBy;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public String getCompanyInstruction() {
        return companyInstruction;
    }

    public void setCompanyInstruction(String companyInstruction) {
        this.companyInstruction = companyInstruction;
    }

    public Integer getVacancy() {
        return vacancy;
    }

    public void setVacancy(Integer vacancy) {
        this.vacancy = vacancy;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
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
        this.RequiredSkill = requiredSkill;
    }

    public String getRequiredLevel() {
        return RequiredLevel;
    }

    public void setRequiredLevel(String requiredLevel) {
        this.RequiredLevel = requiredLevel;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.PaymentAmount = paymentAmount;
    }

    public LocalDateTime getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.PaymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.PaymentMethod = paymentMethod;
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
        this.CancelledAt = cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.CreatedAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return CompletedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.CompletedAt = completedAt;
    }

    public String getCompletedBy() {
        return CompletedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.CompletedBy = completedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = createdBy;
    }

    public LocalDate getPostedAt() {
        return PostedAt;
    }

    public void setPostedAt(LocalDate postedAt) {
        this.PostedAt = postedAt;
    }

    public List<JobTraffic> getJobTraffic() {
        return jobTraffic;
    }

    public void setJobTraffic(List<JobTraffic> jobTraffic) {
        this.jobTraffic = jobTraffic;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", vacancy=" + vacancy +
                ", ageLimit='" + ageLimit + '\'' +
                ", experience='" + experience + '\'' +
                ", company='" + company + '\'' +
                ", companyInstruction='" + companyInstruction + '\'' +
                ", category=" + category +
                ", user=" + user +
                ", jobTraffic=" + jobTraffic +
                ", RequiredSkill='" + RequiredSkill + '\'' +
                ", RequiredLevel='" + RequiredLevel + '\'' +
                ", jobType=" + jobType +
                ", jobStatus=" + jobStatus +
                ", PaymentAmount='" + PaymentAmount + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", PaymentDate=" + PaymentDate +
                ", PaymentMethod='" + PaymentMethod + '\'' +
                ", deadline=" + deadline +
                ", CreatedAt=" + CreatedAt +
                ", CompletedAt=" + CompletedAt +
                ", CompletedBy='" + CompletedBy + '\'' +
                ", PostedAt=" + PostedAt +
                ", CancelledAt=" + CancelledAt +
                ", CreatedBy='" + CreatedBy + '\'' +
                '}';
    }
}
