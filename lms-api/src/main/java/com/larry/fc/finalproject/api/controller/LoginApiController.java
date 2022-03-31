package com.larry.fc.finalproject.api.controller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.LoginReq;
import com.larry.fc.finalproject.api.dto.SignUpReq;
import com.larry.fc.finalproject.api.dto.userinfodto.UserInfoDto;
import com.larry.fc.finalproject.api.service.LoginService;
import com.larry.fc.finalproject.api.service.UserStatisticalChartService;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableService;
import com.larry.fc.finalproject.api.service.userservice.UserInfoQueryService;
import com.larry.fc.finalproject.api.service.userservice.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.larry.fc.finalproject.api.service.LoginService.LOGIN_SESSION_KEY;

@Tag(name = "로그인 api")
@RequiredArgsConstructor
@RestController
public class LoginApiController {
    private final LoginService loginService;
    private final UserInfoService userInfoService;
    private final UserStatisticalChartService userStatisticalChartService;
    private final AllUserTableService allUserTableService;
    private final UserInfoQueryService userInfoQueryService;

    @Operation(description = "회원 가입")
    @PostMapping("/api/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpReq signUpReq, HttpSession httpSession){
        //loginService.signUp(signUpReq, httpSession);
        Long id = loginService.signUp1(signUpReq, httpSession);
        userInfoService.create(AuthUser.of(id));
        userStatisticalChartService.create(AuthUser.of(id));
        allUserTableService.createUserDate(AuthUser.of(id));
        return ResponseEntity.ok().build();
    }

    @Operation(description = "로그인")
    @PostMapping("/api/login")
    public List<UserInfoDto> login(@RequestBody LoginReq loginReq, HttpSession session){
        loginService.login(loginReq, session);
        final Long userId = (Long)session.getAttribute(LOGIN_SESSION_KEY);
        return userInfoQueryService.getUserInfo(userId);
    }

//    @Operation(description = "로그인")
//    @PostMapping("/api/login1")
//    public ResponseEntity<Void> login1(@Parameter @RequestBody LoginReq loginReq){
//        //loginService.login();
//        return ResponseEntity.ok().build();
//    }

    @Operation(description = "로그아웃")
    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        loginService.logout(session);
        return ResponseEntity.ok().build();
    }
}
