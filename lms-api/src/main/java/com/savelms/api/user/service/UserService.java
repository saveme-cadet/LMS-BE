package com.savelms.api.user.service;


import com.savelms.api.user.controller.dto.UserSignUpRequest;
import com.savelms.core.role.RoleEnum;
import com.savelms.core.role.domain.entity.Role;
import com.savelms.core.role.domain.entity.UserRole;
import com.savelms.core.role.domain.repository.RoleRepository;
import com.savelms.core.role.domain.repository.UserRoleRepository;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
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
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * 회원가입
     * @param userSignUpRequest
     * @return
     */
    @Transactional
    public Long validateUserNameAndSignUp(UserSignUpRequest userSignUpRequest) {
        validateUsernameDuplicate(userSignUpRequest.getUsername());
        //첫 회원가입시 유저 상태 저장.
        UserRole userRole = createUnauthorizedUserRole();
        String encodedPassword = bCryptPasswordEncoder.encode(userSignUpRequest.getPassword());
        User user = User.builder()
            .username(userSignUpRequest.getUsername())
            .password(encodedPassword)
            .email(userSignUpRequest.getEmail())
            .nickname(userSignUpRequest.getUsername())
            .build();

        userRole.setUserAndUserRoleToUser(user);
        User savedUser = userRepository.save(user);
        userRoleRepository.save(userRole);
        return savedUser.getId();
    }

    /**
     * 회원가입시 적절한 Role찾아옴.
     * @return
     */
    private UserRole createUnauthorizedUserRole() {
        Role defaultRole = roleRepository.findByName(RoleEnum.UNAUTHORIZED.name()).orElse(null);
        UserRole userRole = UserRole.builder()
            .role(defaultRole)
            .build();
        return userRole;
    }

    /**
     * 중복 아이디일경우 예외 발생
     * @param username
     */
    private void validateUsernameDuplicate(String username) {
        userRepository.findByUsername(username)
            .ifPresent(u -> {
                    throw new RuntimeException("Id : "+ u.getUsername()+"는 이미 사용중입니다.");
                });
    }

}
