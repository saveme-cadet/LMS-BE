package com.savelms.api.user.service;


import com.savelms.api.auth.service.EmailService;
import com.savelms.api.team.service.TeamService;
import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.controller.dto.UserChangeAttendStatusRequest;
import com.savelms.api.user.controller.dto.UserChangeRoleRequest;
import com.savelms.api.user.controller.dto.UserChangeTeamRequest;
import com.savelms.api.user.controller.dto.UserParticipatingIdResponse;
import com.savelms.api.user.controller.dto.UserResponseDto;
import com.savelms.api.user.controller.dto.UserSendUserListResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.api.user.role.service.RoleService;
import com.savelms.core.attendance.domain.entity.Attendance;
import com.savelms.core.calendar.domain.entity.Calendar;
import com.savelms.core.calendar.domain.repository.CalendarRepository;
import com.savelms.core.team.TeamEnum;
import com.savelms.core.team.domain.entity.Team;
import com.savelms.core.team.domain.entity.UserTeam;
import com.savelms.core.team.domain.repository.TeamRepository;
import com.savelms.core.team.domain.repository.UserTeamRepository;
import com.savelms.core.user.AttendStatus;
import com.savelms.core.user.domain.DuplicateUsernameException;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserCustomRepository;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.domain.repository.dto.UserSortRuleDto;
import com.savelms.core.user.emailauth.domain.entity.EmailAuth;
import com.savelms.core.user.emailauth.domain.repository.EmailAuthCustomRepository;
import com.savelms.core.user.emailauth.domain.repository.EmailAuthRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.RoleRepository;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j

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

    private final CalendarRepository calendarRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final EmailAuthCustomRepository emailAuthCustomRepository;

    private final EmailService emailService;

    /**
     * 회원가입
     *
     * @param userSignUpRequest
     * @return
     */
    @Transactional
    public String validateUserNameAndSignUp(UserSignUpRequest userSignUpRequest) {
        validateUsernameDuplicate(userSignUpRequest.getUsername());

        EmailAuth emailAuth = emailAuthRepository.save(
            EmailAuth.builder()
                .email(userSignUpRequest.getUsername() + User.EMAILSUFFIX)
                .authToken(UUID.randomUUID().toString())
                .expired(false)
                .build());

        Role defaultRole = roleService.findByValue(RoleEnum.ROLE_UNAUTHORIZED);
        Team defaultTeam = teamService.findByValue(TeamEnum.NONE);
        Calendar calendar = calendarRepository.findByDate(LocalDate.now())
            .orElseThrow(() ->
                new EntityNotFoundException("오늘의 일정이 없습니다."));
        String encodedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        User defaultUser = User.createDefaultUser(userSignUpRequest.getUsername(), encodedPassword,
            userSignUpRequest.getEmail());
        Attendance.createAttendance(defaultUser, calendar);
        UserRole.createUserRole(defaultUser, defaultRole, "signUpDefault", true);
        UserTeam.createUserTeam(defaultUser, defaultTeam, "signUpDefault", true);

        User savedUser = userRepository.save(defaultUser);

        emailService.send(emailAuth.getEmail(), savedUser.getApiId(), emailAuth.getAuthToken());
        return savedUser.getApiId();
    }


    /**
     * 중복 아이디일경우 예외 발생
     *
     * @param username
     */
    private void validateUsernameDuplicate(String username) {
        userRepository.findByUsername(username)
            .ifPresent(u -> {
                throw new DuplicateUsernameException("Id : " + u.getUsername() + "는 이미 사용중입니다.");
            });
    }


    public ListResponse<UserResponseDto> findUserList(Long offset, Long size) {

        List<User> users = userCustomRepository.findAllAndSortAndPage(offset, size);

        List<UserResponseDto> userResponseDtoDatas = users.stream()
            .map((u) ->
                UserResponseDto.builder()
                    .id(u.getApiId())
                    .attendStatus(u.getAttendStatus())
                    .nickname(u.getNickname())
                    .role(userRoleRepository.currentlyUsedUserRole(u.getId())
                        .get(0)
                        .getRole()
                        .getValue())
                    .team(userTeamRepository.currentlyUsedUserTeam(u.getId())
                        .get(0)
                        .getTeam()
                        .getValue())
                    .vacation(u.getVacations().stream()
                        .map(v ->
                            v.getRemainingDays() - v.getUsedDays())
                        .reduce(0L, Long::sum))
                    .build())
            .collect(Collectors.toList());
        return ListResponse.<UserResponseDto>builder()
            .count(userResponseDtoDatas.size())
            .content(userResponseDtoDatas)
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

        user.originalUserRolesCurrentlyUsedToFalse(originalUserRoles);
        UserRole userRole = UserRole.createUserRole(user, role, request.getReason(), true);
        userRoleRepository.save(userRole);
        user.setNewCurrentlyUsedUserRole(userRole);
        return user.getApiId();
    }

    @Transactional
    public String changeAttendStatus(String apiId, UserChangeAttendStatusRequest request) {
        User user = userRepository.findByApiId(apiId).orElseThrow(() ->
            new EntityNotFoundException("apiId에 해당하는 user가 없습니다."));
        user.changeAttendStatus(request.getAttendStatus());
        return user.getApiId();
    }

    public ListResponse<UserParticipatingIdResponse> findParticipatingUserList() {

        List<UserParticipatingIdResponse> responseContents = userRepository.findAllByAttendStatus(
                AttendStatus.PARTICIPATED)
            .stream()
            .map((u) ->
                UserParticipatingIdResponse.builder()
                    .userId(u.getApiId())
                    .username(u.getUsername())
                    .build())
            .collect(Collectors.toList());
        return ListResponse.<UserParticipatingIdResponse>builder()
            .count(responseContents.size())
            .content(responseContents)
            .build();
    }
}
