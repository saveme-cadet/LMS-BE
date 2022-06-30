package com.savelms.api.user.service;


import com.savelms.api.user.controller.dto.UserSendUserListResponse;
import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.core.user.domain.repository.dto.UserSortRuleDto;
import com.savelms.api.user.role.service.RoleService;
import com.savelms.core.user.domain.repository.UserCustomRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import java.util.List;
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

    /**
     * 회원가입
     *
     * @param userSignUpRequest
     * @return
     */
    @Transactional
    public String validateUserNameAndSignUp(UserSignUpRequest userSignUpRequest) {
        validateUsernameDuplicate(userSignUpRequest.getUsername());

        Role defaultRole = roleService.findByName(RoleEnum.ROLE_UNAUTHORIZED.name());
        UserRole userRole = createUnauthorizedUserRole(defaultRole);
        String encodedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        User defaultUser = User.createDefaultUser(userSignUpRequest.getUsername(), encodedPassword,
            userSignUpRequest.getEmail());

        userRole.setUserAndUserRoleToUser(defaultUser);

        User savedUser = userRepository.save(defaultUser);
        userRoleRepository.save(userRole);
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

    @Transactional
    public void update() {

    }

    public UserSendUserListResponse findUserList(Boolean attendStatus, Long offset, Long size,
        String sortRule) {

        UserSortRuleDto userSortRuleDto = UserSortRuleDto.toUserSortRoleDto(sortRule);

        List<User> users = userCustomRepository.findAllAndSortAndPage(attendStatus, offset, size,
            userSortRuleDto);
        return UserSendUserListResponse.builder()
            .users(users)
            .count(users.size())
            .build();
    }
}
