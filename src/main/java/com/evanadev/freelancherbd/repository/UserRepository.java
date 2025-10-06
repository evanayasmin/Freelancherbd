package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(long id);

    User findByUsername(String username);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN FETCH u.userProfile up
        JOIN u.roles r
        WHERE r.name = 'ROLE_FREELANCER'
        """)
    List<User> findAllFreelancers();

    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN FETCH u.userProfile up
        JOIN u.roles r
        WHERE r.name = 'Role_EMPLOYER'
        """)
    List<User> findAllClients();
}
