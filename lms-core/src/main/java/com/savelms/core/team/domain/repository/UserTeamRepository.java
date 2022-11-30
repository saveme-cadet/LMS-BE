package com.savelms.core.team.domain.repository;

import com.savelms.core.team.domain.entity.UserTeam;

import java.time.LocalDateTime;
import java.util.List;

import com.savelms.core.user.AttendStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.currentlyUsed = true")
    List<UserTeam> currentlyUsedUserTeam(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT ut FROM UserTeam ut WHERE ut.createdAt <= :date ORDER BY ut.createdAt desc")
    List<UserTeam> findAllByDate(@Param("date") LocalDateTime date);

    @Query("SELECT ut FROM UserTeam ut " +
            "JOIN FETCH ut.user u " +
            "WHERE ut.createdAt <= :date " +
            "ORDER BY ut.createdAt desc")
    List<UserTeam> findAllByDateAndAttendStatus(
            @Param("date") LocalDateTime date);

    //List<UserTeam> findAllByCreatedAt(LocalDateTime date);
}
