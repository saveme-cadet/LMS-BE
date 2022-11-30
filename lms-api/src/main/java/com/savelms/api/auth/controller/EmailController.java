package com.savelms.api.auth.controller;


import com.savelms.api.auth.controller.dto.EmailAuthRequestDto;
import com.savelms.api.auth.service.EmailService;
import com.savelms.api.commondata.APIDataResponse;
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

    @PostMapping("/auth/password-inquery")
    public APIDataResponse<ResponseEntity<Void>> passwordInquery(@Validated @Parameter @RequestBody UserPasswordInqueryRequest request) {
        userService.passwordInquery(request.getUsername());

        return APIDataResponse.of(ResponseEntity.status(HttpStatus.OK).body(null));
    }
    @GetMapping("/auth/email")
    public APIDataResponse<ResponseEntity<String>> confirmEmail(@Validated @ModelAttribute EmailAuthRequestDto requestDto) {
        try {
            if (requestDto.getAuthorizationType().equals(EmailAuthRequestDto.SIGNUP)) {
                emailService.confirmEmailAndAuthorizeUser(requestDto);
            } else if (requestDto.getAuthorizationType().equals(EmailAuthRequestDto.RESET)) {
                emailService.confirmEmailAndUpdateWithRandomPassword(requestDto);
            }
        } catch (EmailAuthTokenNotFoundException eatnfe) {
            return APIDataResponse.of(ResponseEntity.status(HttpStatus.FORBIDDEN).body("이메일 인증에 실패하였거나, 이미 만료된 토큰 사용했기 때문에 인증이 진행되지않습니다."));
        }
        return APIDataResponse.of(ResponseEntity.status(HttpStatus.OK).body("이메일 인증에 성공하였습니다."));
    }

}

