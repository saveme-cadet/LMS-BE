package com.savelms.api.user.userteam.service;


import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.UserTeam;
import com.savelms.core.user.domain.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserTeamService {


    private final UserRepository userRepository;

    //모든 유저의 특정날짜 team을 반환.
    @Data
    @AllArgsConstructor
    private static class ForMapping {

        private Long Id;
        private TeamEnum teamEnum;
    }
    //모든 유저의 특정날짜 team을 반환.
    public Map<Long, TeamEnum> findAllUserTeamByDate(LocalDate date) {

        return userRepository.findAllWithUserTeamsAndTeam()
            .stream()
            .map((u) -> (
                new UserTeamService.ForMapping(u.getId(),
                    findNearestUserTeam(u.getUserTeams(), date))
            ))
            .collect(Collectors.toMap(
                UserTeamService.ForMapping::getId,
                UserTeamService.ForMapping::getTeamEnum
            ));
    }

    private TeamEnum findNearestUserTeam(List<UserTeam> userTeams, LocalDate date) {

        TeamEnum result = null;
        LocalDateTime findDay = date.plusDays(1L).atStartOfDay();
        LocalDateTime resultTime = LocalDateTime.MIN;
        for (UserTeam userTeam : userTeams) {
            LocalDateTime createdAt = userTeam.getCreatedAt();
            if (createdAt.isBefore(findDay)) {
                if (createdAt.compareTo(resultTime) > 0) {
                    result = userTeam.getTeam().getValue();
                    resultTime = createdAt;
                }
            }
        }
        return result;
    }

}
