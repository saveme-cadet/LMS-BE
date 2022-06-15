package com.larry.fc.finalproject.api.controller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.SignUpReq;
import com.larry.fc.finalproject.api.service.LoginService;
import com.larry.fc.finalproject.api.service.UserStatisticalChartService;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableService;
import com.larry.fc.finalproject.api.service.userservice.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Tag(name = "유저 생성")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AllInfoController {
    private final LoginService loginService;
    private final UserInfoService userInfoService;
    private final UserStatisticalChartService userStatisticalChartService;
    private final AllUserTableService allUserTableService;

//    @Operation(description = "user id 생성과 동시에 userinfo, statisticalChart 생성 ")
//    @PostMapping("/user")
//    public ResponseEntity<Void> makeUserAndUserinfo(@Parameter @RequestBody SignUpReq signUpReq, HttpSession httpSession){
//        Long id = loginService.signUp1(signUpReq, httpSession);
//        userInfoService.create(AuthUser.of(id));
//        userStatisticalChartService.create(AuthUser.of(id));
//        allUserTableService.createUserDate(AuthUser.of(id));
//        return ResponseEntity.ok().build();
//
//    }
}
