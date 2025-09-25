package com.evanadev.freelancherbd.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String categoryName;
    String description;

    @Enumerated(EnumType.STRING) // store enum as string in DB (e.g., "ACTIVE", "INACTIVE")
    private Status status = Status.ACTIVE;

    @Column(updatable = false) // once created, won’t change on update
    LocalDateTime creationDate;

    // Auto set before inserting
    @PrePersist
    protected void onCreate() {

        this.creationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
