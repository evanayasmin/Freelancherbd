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

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStaus;
    private String remarks;

    @LastModifiedDate
    @Column(updatable = false)
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

    public ApplicationStatus getApplicationStaus() {
        return applicationStaus;
    }

    public void setApplicationStaus(ApplicationStatus applicationStaus) {
        this.applicationStaus = applicationStaus;
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
}
