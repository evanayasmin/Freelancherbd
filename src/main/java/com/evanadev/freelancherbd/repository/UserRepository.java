package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(long id);

    User findByUsername(String username);
}
