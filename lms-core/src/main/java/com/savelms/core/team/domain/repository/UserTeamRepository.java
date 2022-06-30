package com.savelms.core.team.domain.repository;

import com.savelms.core.team.domain.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

}
