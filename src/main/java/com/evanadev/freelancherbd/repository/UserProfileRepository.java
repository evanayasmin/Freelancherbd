package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

}
