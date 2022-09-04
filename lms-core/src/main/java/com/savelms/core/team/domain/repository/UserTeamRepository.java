package com.savelms.core.team.domain.repository;

import com.savelms.core.team.domain.entity.UserTeam;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.currentlyUsed = true")
    List<UserTeam> currentlyUsedUserTeam(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT ut FROM UserTeam ut WHERE ut.createdAt <= :date order by ut.createdAt desc")
    List<UserTeam> findAllByDateBefore(@Param("date") LocalDateTime date);

}
