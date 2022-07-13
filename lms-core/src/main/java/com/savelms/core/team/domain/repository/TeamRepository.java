package com.savelms.core.team.domain.repository;

import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByValue(TeamEnum teamEnum);
}
