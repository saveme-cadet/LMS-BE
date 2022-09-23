package com.savelms.api.user.userrole.service;

import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public Map<Long, RoleEnum> findAllUserRoleByDate(LocalDate date) {
        Map<Long, RoleEnum> userRoles = new HashMap<>();
        Map<User, List<UserRole>> userAndUserRoles = userRoleRepository.findAllByDateBefore(LocalDateTime.of(date, LocalTime.MAX))
                .stream().collect(Collectors.groupingBy(UserRole::getUser));

        for (User user : userAndUserRoles.keySet()) {
            UserRole userRole = userAndUserRoles.get(user).stream().max(Comparator.comparing(UserRole::getCreatedAt)).get();
            userRoles.put(user.getId(), userRole.getRole().getValue());
        }
        return userRoles;
    }
}
