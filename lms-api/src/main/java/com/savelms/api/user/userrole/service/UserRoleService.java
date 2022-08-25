package com.savelms.api.user.userrole.service;


import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class UserRoleService {

    private final UserRepository userRepository;

    @Data
    @AllArgsConstructor
    private static class ForMapping {

        private Long Id;
        private RoleEnum roleEnum;
    }
    //모든 유저의 특정날짜 role을 반환.
    public Map<Long, RoleEnum> findAllUserRoleByDate(LocalDate date) {

        return userRepository.findAll()
            .stream()
            .map((u) -> (
                new ForMapping(u.getId(),
                    findNearestUserRole(u.getUserRoles(), date))
            ))
            .collect(Collectors.toMap(
                ForMapping::getId,
                ForMapping::getRoleEnum
            ));
    }

    private RoleEnum findNearestUserRole(Set<UserRole> userRoles, LocalDate date) {

        RoleEnum result = null;
        LocalDateTime findDay = date.plusDays(1L).atStartOfDay();
        LocalDateTime resultTime = LocalDateTime.MIN;
        for (UserRole userRole : userRoles) {
            LocalDateTime createdAt = userRole.getCreatedAt();
            if (createdAt.isBefore(findDay)) {
                if (createdAt.compareTo(resultTime) > 0) {
                    result = userRole.getRole().getValue();
                    resultTime = createdAt;
                }
            }
        }
        return result;
    }

}
