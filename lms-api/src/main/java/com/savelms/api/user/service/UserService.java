package com.savelms.api.user.service;


import com.savelms.api.team.service.TeamService;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusRequest;
import com.savelms.api.user.controller.dto.UserChangeRoleRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserResponseDto;
import com.savelms.api.user.controller.dto.UserSendUserListResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.Team;
import com.savelms.core.team.domain.entity.UserTeam;
import com.savelms.core.team.domain.repository.TeamRepository;
import com.savelms.core.team.domain.repository.UserTeamRepository;
import com.savelms.core.user.domain.repository.dto.UserSortRuleDto;
import com.savelms.api.user.role.service.RoleService;
import com.savelms.core.user.domain.repository.UserCustomRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.RoleRepository;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final RoleService roleService;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    private final TeamService teamService;
    private final UserTeamRepository userTeamRepository;
    /**
     * 회원가입
     *
     * @param userSignUpRequest
     * @return
     */
    @Transactional
    public String validateUserNameAndSignUp(UserSignUpRequest userSignUpRequest) {
        validateUsernameDuplicate(userSignUpRequest.getUsername());

        Role defaultRole = roleService.findByValue(RoleEnum.ROLE_UNAUTHORIZED);
        Team defaultTeam = teamService.findByValue(TeamEnum.RED);
        String encodedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        User defaultUser = User.createDefaultUser(userSignUpRequest.getUsername(), encodedPassword,
            userSignUpRequest.getEmail());
        UserRole.createUserRole(defaultUser, defaultRole, "signUpDefault", true);
        UserTeam.createUserTeam(defaultUser, defaultTeam, "signUpDefault", true);

        User savedUser = userRepository.save(defaultUser);
        return savedUser.getApiId();
    }

    /**
     * 회원가입시 적절한 Role찾아옴.
     *
     * @return
     */
    private UserRole createUnauthorizedUserRole(Role defaultRole) {

        return UserRole.builder()
            .role(defaultRole)
            .build();
    }

    /**
     * 중복 아이디일경우 예외 발생
     *
     * @param username
     */
    private void validateUsernameDuplicate(String username) {
        userRepository.findByUsername(username)
            .ifPresent(u -> {
                throw new RuntimeException("Id : " + u.getUsername() + "는 이미 사용중입니다.");
            });
    }


    public UserSendUserListResponse findUserList(Boolean attendStatus, Long offset, Long size,
        String sortRule) {

        UserSortRuleDto userSortRuleDto = UserSortRuleDto.toUserSortRoleDto(sortRule);

        List<User> users = userCustomRepository.findAllAndSortAndPage(attendStatus, offset, size,
            userSortRuleDto);

        List<UserResponseDto> userDtos = users.stream()
            .map((u) ->
                UserResponseDto.builder()
                    .nickname(u.getNickname())
                    .apiId(u.getApiId())
                    .build())
            .collect(Collectors.toList());
        return UserSendUserListResponse.builder()
            .users(userDtos)
            .count(userDtos.size())
            .build();
    }

    @Transactional
    public String changeTeam(String apiId, UserChangeTeamRequest request) {
        User user = userRepository.findByApiId(apiId).orElseThrow(() ->
            new EntityNotFoundException("apiId에 해당하는 user가 없습니다."));

        TeamEnum teamEnum = request.getTeam();
        Team team = teamRepository.findByValue(teamEnum).orElseThrow(() ->
            new EntityNotFoundException(teamEnum.name() + "에 해당하는 team이 없습니다."));

        List<UserTeam> originalUserTeams = userTeamRepository.currentlyUsedUserTeam(user.getId());

        user.originalUserTeamsCurrentlyUsedToFalse(originalUserTeams);
        UserTeam userTeam = UserTeam.createUserTeam(user, team, request.getReason(), true);
        userTeamRepository.save(userTeam);
        user.setNewCurrentlyUsedUserTeam(userTeam);
        return user.getApiId();
    }

    @Transactional
    public String changeRole(String apiId, UserChangeRoleRequest request) {
        User user = userRepository.findByApiId(apiId).orElseThrow(() ->
            new EntityNotFoundException("apiId에 해당하는 user가 없습니다."));
        RoleEnum roleEnum = request.getRole();

        Role role = roleRepository.findByValue(roleEnum).orElseThrow(() ->
            new EntityNotFoundException(roleEnum.name() + "에 해당하는 role이 없습니다."));
        List<UserRole> originalUserRoles = userRoleRepository.currentlyUsedUserRole(user.getId());

        user.originalUserRolesCurrentlyUsedToFalse( originalUserRoles);
        UserRole userRole = UserRole.createUserRole(user, role, request.getReason(), true);
        userRoleRepository.save(userRole);
        user.setNewCurrentlyUsedUserRole(userRole);
        return user.getApiId();
    }

    public String changeAttendStatus(String apiId, UserChangeAttendStatusRequest request) {
        User user = userRepository.findByApiId(apiId).orElseThrow(() ->
            new EntityNotFoundException("apiId에 해당하는 user가 없습니다."));
        user.changeAttendStatus(request.getAttendStatus());
        return user.getApiId();
    }
}
