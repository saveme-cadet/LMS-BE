package com.larry.fc.finalproject.api.controller.useradmincontroller;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.userdto.UserAttendenceDto;
import com.larry.fc.finalproject.api.dto.userdto.UserTeamAndRoleDto;
import com.larry.fc.finalproject.api.dto.userinfodto.*;
import com.larry.fc.finalproject.api.service.userservice.UserInfoQueryService;
import com.larry.fc.finalproject.api.service.userservice.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "유저 정보")
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
@RestController
@Validated
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserInfoQueryService userInfoQueryService;

    @Operation(description = "유저 모든 정보 가져오기")
    @GetMapping("/allinfo")
    public List<UserInfoDto> getUserInfoByDay(
            @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){

        return userInfoQueryService.getUserInfoByDay(authUser);
    }

    @Operation(description = "유저 정보 일부 가져오기")
    @GetMapping("/weekinfo")
    public List<UserInfoWeekDto> getUserInfoWeek(
            @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        return userInfoQueryService.getUserInfoWeek(authUser);
    }

    @Operation(description = "유저 정보 일부 가져오기")
    @GetMapping("/monthinfo")
    public List<UserInfoMonthDto> getUserInfoMonth(
            @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        return userInfoQueryService.getUserInfoMonth(authUser);
    }

    @Operation(description = "모든 유저 정보 가져오기")
    @GetMapping("/all")
    public List<UserInfoDto> getAllUserInfo(){
        return userInfoQueryService.getAllUserInfo();
    }

    @Operation(description = "유저 정보 모두 수정")
    @PutMapping("/all")
    public ResponseEntity<Void> updateUserInfo(@Parameter @RequestBody UserInfoDto userInfoDto,
                                               @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        userInfoService.update(userInfoDto, authUser);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "매달 1일 유저 출결 상태, 출결횟수, 레벨 초기화 및 상승")
    @PutMapping()
    public  ResponseEntity<Void> updateUserMonthInfo(){
        userInfoService.updateMonthUser();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 정보 week 수정")
    @PutMapping("/week")
    public ResponseEntity<Void> updateUserInfo(@Parameter @RequestBody UserInfoWeekDto userInfoDto,
                                               @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        userInfoService.updateWeek(userInfoDto, authUser);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 정보 month 수정")
    @PutMapping("/month")
    public ResponseEntity<Void> updateUserInfo(@Parameter @RequestBody UserInfoMonthDto userInfoDto,
                                               @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        userInfoService.updateMonth(userInfoDto, authUser);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 정보 참석 여부 수정")
    @PutMapping("/attendstatus")
    public ResponseEntity<Void> updateUserAttendStatus(@RequestBody UserAttendenceDto userAttendenceDto){
        userInfoService.updateAttendStatus(userAttendenceDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 팀 역할 즉시 수정")
    @PutMapping("/teamandrole")
    public ResponseEntity<Void> updateUserTeamAndRole(@RequestBody UserTeamAndRoleDto userTeamAndRoleDto){
        userInfoService.updateTeamAndRole(userTeamAndRoleDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 팀 수정")
    @PutMapping("/team")
    public ResponseEntity<Void> updateUserTeam(@RequestBody UserTeamChangeDto userTeamChangeDto){
        userInfoService.updateUserTeam(userTeamChangeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 역할 수정")
    @PutMapping("/role")
    public ResponseEntity<Void> updateUserRole(@RequestBody UserRoleChangeDto userRoleChangeDto){
        userInfoService.updateUserRole(userRoleChangeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "유저 휴가 일 수 + 0.5")
    @PutMapping("/vacationplus")
    public ResponseEntity<Void> updateUserVacationPlus(@RequestBody UserVacationChangeDto userVacationChangeDto){
        userInfoService.updateUserVacationPlus(userVacationChangeDto);
        return ResponseEntity.ok().build();
    }


    @Operation(description = "유저 휴가 일 수 - 0.5")
    @PutMapping("/vacationminus")
    public ResponseEntity<Void> updateUserVacationMinus(@RequestBody UserVacationChangeDto userVacationChangeDto){
        userInfoService.updateUserVacationMinus(userVacationChangeDto);
        return ResponseEntity.ok().build();
    }

    @Operation (description = "aoji 점수 매달 마지막 attendscore 와 합산")
    @PutMapping("/MonthScoreOfAllUserAttendScore")
    public ResponseEntity<Void> MonthScoreOfAllUserAttendScore(){
        userInfoService.updateMonthOfUserAttendScore();
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteUserInfo(@Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
//        try{
//            userInfoService.delete(authUser);
//            return ResponseEntity.ok().build();
//        } catch (Exception e){
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
