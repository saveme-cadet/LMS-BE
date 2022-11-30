package com.savelms.api.team.service;

import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.Team;
import com.savelms.core.team.domain.repository.TeamRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamService {

    private final TeamRepository teamRepository;


    public Team findByValue(TeamEnum teamEnum) {
        return teamRepository.findByValue(teamEnum)
            .orElseThrow(()->
                new EntityNotFoundException("해당하는 team이 없습니다."));
    }
}
