package com.savelms.api.auth.service;

import com.savelms.api.auth.controller.dto.EmailAuthRequestDto;
import com.savelms.api.auth.controller.dto.EmailAuthResponseDto;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.emailauth.domain.EmailAuthTokenNotFoundException;
import com.savelms.core.user.emailauth.domain.entity.EmailAuth;
import com.savelms.core.user.emailauth.domain.repository.EmailAuthCustomRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import com.savelms.core.user.role.domain.entity.UserRole;
import com.savelms.core.user.role.domain.repository.RoleRepository;
import com.savelms.core.user.role.domain.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private static final String HOST = "http://localhost:8080";
    private static final String PATH = "/api/auth/email";

    private final JavaMailSender javaMailSender;

    private final EmailAuthCustomRepository emailAuthCustomRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    @Async
    public void send(String email, String apiId, String authToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject("42Seoul 구해줘 카뎃 회원가입 이메일 인증입니다. 다음링크를 클릭하면 인증이 완료됩니다. 6개월후 만료됩니다.");
        smm.setText(HOST+PATH+"?id="+apiId
            + "&email=" + email
            + "&authToken="+authToken);

        javaMailSender.send(smm);
    }

    @Transactional
    public EmailAuthResponseDto confirmEmail(EmailAuthRequestDto requestDto) {
        EmailAuth emailAuth = emailAuthCustomRepository.findValidAuthByEmail(requestDto.getEmail(), requestDto.getAuthToken(), LocalDateTime.now())
            .orElseThrow(EmailAuthTokenNotFoundException::new);
        User user = userRepository.findByApiId(requestDto.getId())
            .orElseThrow(EntityNotFoundException::new);
        Role role = roleRepository.findByValue(RoleEnum.ROLE_USER)
            .orElseThrow(EntityNotFoundException::new);

        emailAuth.useToken();
        user.emailVerifiedSuccess();

        UserRole userRoleUser = UserRole.createUserRole(user, role, "email authentication",
            true);
        List<UserRole> userRoles = userRoleRepository.currentlyUsedUserRole(user.getId());
        userRoles.get(0).notCurrentlyUsed();
        user.getUserRoles().add(userRoleUser);

        return EmailAuthResponseDto.builder()
            .userId(user.getApiId())
            .build();
    }
}
