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
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String HOST = "https://www.savvemecadet.click";
    private final JavaMailSender javaMailSender;

    private final EmailAuthCustomRepository emailAuthCustomRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Async
    public void send(String email, String subject, String text) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject(subject);
        smm.setText(text);

        javaMailSender.send(smm);
    }

    @Transactional
    public EmailAuthResponseDto confirmEmailAndAuthorizeUser(EmailAuthRequestDto requestDto) {
        Optional<EmailAuth> validAuthByEmail = emailAuthCustomRepository.findValidAuthByEmail(
            requestDto.getEmail(),
            requestDto.getAuthToken(), LocalDateTime.now());
        EmailAuth emailAuth = validAuthByEmail.orElseThrow(EmailAuthTokenNotFoundException::new);
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

    @Transactional
    public EmailAuthResponseDto confirmEmailAndUpdateWithRandomPassword(EmailAuthRequestDto requestDto) {
        Optional<EmailAuth> validAuthByEmail = emailAuthCustomRepository.findValidAuthByEmail(
            requestDto.getEmail(),
            requestDto.getAuthToken(), LocalDateTime.now());
        EmailAuth emailAuth = validAuthByEmail.orElseThrow(EmailAuthTokenNotFoundException::new);

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

        String randomPassword = User.getRamdomPassword(10);
        user.updatePassword(bCryptPasswordEncoder.encode(randomPassword));

        send(emailAuth.getEmail(),"42Seoul 구해줘 카뎃 비밀번호 찾기를 통해 임시비밀번호가 발급되었습니다. 비밀번호 변경을 통해 비밀번호를 변경해주세요.",
            "아이디 : " + user.getUsername() + System.lineSeparator()
                + "임시비밀번호 : " + randomPassword);

        return EmailAuthResponseDto.builder()
            .userId(user.getApiId())
            .build();
    }

}
