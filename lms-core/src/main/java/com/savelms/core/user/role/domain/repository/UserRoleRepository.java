package com.savelms.core.user.role.domain.repository;

import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.role.domain.entity.UserRole;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.currentlyUsed = true")
    List<UserRole> currentlyUsedUserRole(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT ur FROM UserRole ur WHERE ur.createdAt <= :date ORDER BY ur.createdAt DESC")
    List<UserRole> findAllByDate(@Param("date") LocalDateTime date);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.user u " +
            "WHERE ur.createdAt <= :date AND u.attendStatus = :attendStatus " +
            "ORDER BY ur.createdAt DESC")
    List<UserRole> findAllByDateAndAttendStatus(
            @Param("date") LocalDateTime date,
            @Param("attendStatus") AttendStatus attendStatus);

    List<UserRole> findAllByCreatedAt(LocalDateTime date);
}
