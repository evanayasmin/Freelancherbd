package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.JobTraffic;
import com.evanadev.freelancherbd.model.TrafficType;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.model.UserTrac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTrackRepository extends JpaRepository<UserTrac, Long>{

    Optional<UserTrac> findByIdAndUser(long id, User user);

    @Query("""
    SELECT DISTINCT t
    FROM UserTrac t
    WHERE t.user.id = :trackId 
      AND t.loggedinUser.id = :userId 
      AND t.trafficType = :trafficType
""")
    Optional<UserTrac> findByUserIdLoggedId(
            @Param("userId") Long userId,
            @Param("trackId") Long trackId,
            @Param("trafficType") TrafficType trafficType
    );
}
