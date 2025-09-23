package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    // Finds the profile by userId (returns Optional so you can check existence)
    Optional<UserProfile> findByUserId(Long userId);

    // Or if you only need a check (true/false)
    boolean existsByUserId(Long userId);

}
