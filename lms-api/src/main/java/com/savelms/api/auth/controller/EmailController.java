package com.savelms.api.auth.controller;


import com.savelms.api.auth.controller.dto.EmailAuthRequestDto;
import com.savelms.api.auth.service.EmailService;
import com.savelms.core.user.emailauth.domain.EmailAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class EmailController {

    private final EmailService emailService;

    @GetMapping("/auth/email")
    public ResponseEntity<String> confirmEmail(@ModelAttribute EmailAuthRequestDto requestDto) {
        try {
            emailService.confirmEmail(requestDto);
        } catch (EmailAuthTokenNotFoundException eatnfe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이메일 인증에 실패햐였습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("이메일 인증에 성공하였습니다.");
    }
}

