package com.larry.fc.finalproject.api.controller;

import com.larry.fc.finalproject.api.dto.LoginReq;
import com.larry.fc.finalproject.api.dto.SignUpReq;
import com.larry.fc.finalproject.api.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Tag(name = "로그인 api")
@RequiredArgsConstructor
@RestController
public class LoginApiController {
    private final LoginService loginService;

//    @Operation(description = "회원 가입")
//    @PostMapping("/api/sign-up")
//    public ResponseEntity<Void> signUp(@Parameter @RequestBody SignUpReq signUpReq, HttpSession session){
//        loginService.signUp(signUpReq, session);
//        return ResponseEntity.ok().build();
//    }

    @Operation(description = "로그인")
    @PostMapping("/api/login")
    public ResponseEntity<Void> login(@Parameter @RequestBody LoginReq loginReq, HttpSession session){
        loginService.login(loginReq, session);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "로그아웃")
    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        loginService.logout(session);
        return ResponseEntity.ok().build();
    }
}
