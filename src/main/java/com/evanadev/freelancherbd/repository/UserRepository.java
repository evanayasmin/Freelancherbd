package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
        LEFT JOIN FETCH u.userProfile up
        JOIN u.roles r
        WHERE r.name = 'ROLE_FREELANCER'
        """)
    List<User> findAllFreelancers();

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.userProfile up
        JOIN u.roles r
        WHERE r.name = 'Role_EMPLOYER'
        """)
    List<User> findAllClients();

    @Query("""
    SELECT DISTINCT u
    FROM User u
    LEFT JOIN FETCH u.userProfile up
    JOIN u.roles r
    WHERE u.id = :userid
    """)
    Optional<User> findUserDetails(@Param("userid") Long userid);

    User findByEmail(String email);

    @Query("""
    SELECT u
    FROM User u
    JOIN u.roles r
    WHERE r.name = 'ROLE_ADMIN'
    """)
    Optional<User> findAdminUser();

    @Query("""
    SELECT DISTINCT u
    FROM User u
    LEFT JOIN u.userProfile p
    WHERE 
    (
        (:skillTitle IS NOT NULL AND LOWER(p.skills) LIKE LOWER(CONCAT('%', :skillTitle, '%')))
        OR
        (:title IS NOT NULL AND LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))
        OR
        (:jobType IS NOT NULL AND LOWER(p.availability) LIKE LOWER(CONCAT('%', :jobType, '%')))
    )
    AND (
        :skillTitle IS NOT NULL 
        OR :title IS NOT NULL
        OR :jobType IS NOT NULL
       )
""")
    List<User> searchFreelancers(
            @Param("skillTitle") String skillTitle,
            @Param("title") String title,
            @Param("jobType") String jobType,
            @Param("requiredLevel") String requiredLevel
    );
}
