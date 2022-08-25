package com.savelms.core.user.domain.repository;

import com.savelms.core.attendance.domain.AttendanceStatus;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.entity.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByApiId(String apiId);
    List<User> findAllByAttendStatus(AttendStatus ch);

    @Query("SELECT DISTINCT u FROM User u join fetch u.userTeams ut join fetch ut.team ")
    List<User> findAllWithUserTeamsAndTeam();


}
