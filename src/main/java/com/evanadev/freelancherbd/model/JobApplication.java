package com.evanadev.freelancherbd.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Lob
    @Column(columnDefinition = "LongText")
    private String coverLetter;

    @Column(nullable = false)
    private String expectedSalary;

    @Column(nullable = false)
    private Double approvedSalary;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    private String remarks;

    private LocalDateTime jobAssignedDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime applicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private User user;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public Double getApprovedSalary() {
        return approvedSalary;
    }

    public void setApprovedSalary(Double approvedSalary) {
        this.approvedSalary = approvedSalary;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getJobAssignedDate() {
        return jobAssignedDate;
    }

    public void setJobAssignedDate(LocalDateTime jobAssignedDate) {
        this.jobAssignedDate = jobAssignedDate;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.applicationDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "Id=" + Id +
                ", job=" + job +
                ", coverLetter='" + coverLetter + '\'' +
                ", expectedSalary='" + expectedSalary + '\'' +
                ", applicationStatus=" + applicationStatus +
                ", remarks='" + remarks + '\'' +
                ", jobAssignedDate=" + jobAssignedDate +
                ", applicationDate=" + applicationDate +
                ", user=" + user +
                '}';
    }
}
