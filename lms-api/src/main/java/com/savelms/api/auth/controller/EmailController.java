package com.savelms.api.auth.controller;


import com.savelms.api.auth.controller.dto.EmailAuthRequestDto;
import com.savelms.api.auth.service.EmailService;
import com.savelms.api.user.controller.dto.UserPasswordInqueryRequest;
import com.savelms.api.user.service.UserService;
import com.savelms.core.user.emailauth.domain.EmailAuthTokenNotFoundException;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @ExceptionHandler
    public ResponseEntity<String> handleException(EmailAuthTokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @PostMapping("/auth/password-inquery")
    public ResponseEntity<Void> passwordInquery(@Validated @Parameter @RequestBody UserPasswordInqueryRequest request) {
        userService.passwordInquery(request.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/auth/email")
    public ResponseEntity<String> confirmEmail(@Validated @ModelAttribute EmailAuthRequestDto requestDto) {
        try {
            if (requestDto.getAuthorizationType().equals(EmailAuthRequestDto.SIGNUP)) {
                emailService.confirmEmailAndAuthorizeUser(requestDto);
            } else if (requestDto.getAuthorizationType().equals(EmailAuthRequestDto.RESET)) {
                emailService.confirmEmailAndUpdateWithRandomPassword(requestDto);
            }
        } catch (EmailAuthTokenNotFoundException eatnfe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이메일 인증에 실패햐였습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("이메일 인증에 성공하였습니다.");
    }

}

