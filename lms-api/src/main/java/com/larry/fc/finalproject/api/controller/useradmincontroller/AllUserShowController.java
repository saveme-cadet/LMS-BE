package com.larry.fc.finalproject.api.controller.useradmincontroller;

import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.dto.tabledto.AllTableDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckInDto;
import com.larry.fc.finalproject.api.dto.tabledto.TableCheckOutDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserRoleChangeDto;
import com.larry.fc.finalproject.api.dto.userinfodto.UserTeamChangeDto;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableQueryService;
import com.larry.fc.finalproject.api.service.userservice.AllUserTableService;
import com.larry.fc.finalproject.core.domain.entity.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "당일 모든 학생 정보")
@RequiredArgsConstructor
@RequestMapping("/api/alltable")
@RestController
public class AllUserShowController {

    private final AllUserTableService allUserTableService;
    private final AllUserTableQueryService allUserTableQueryService;


    @Operation(description = "오늘 학생 정보 생성 요일 추가 테스트용")
    @PostMapping("/saveshowtable")
    public ResponseEntity<Void> createTable(@Parameter (name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        allUserTableService.createAllDate(date);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "오늘 모든 학생 정보 생성 - 매일 00:00 AM 자동 갱신되니 건드릴 필요 없음")
    @PostMapping("/creatable")
    public ResponseEntity<Void> createTable1(){
        allUserTableService.createAll();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "오늘 학생들 정보 불러오기")
    @GetMapping("/day")
    public List<AllTableDto> getUserTableByDay(
            @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, AuthUser authUser){
        return allUserTableQueryService.getDayTableByDay(date == null ? LocalDate.now() : date);
    }

    @Operation(description = "그 날 출석표 정보 수정하기")
    @PutMapping("/userinfoall")
    public ResponseEntity<Void> updateUserInfoAll(@Parameter @RequestBody AllTableDto allTableDto){
        allUserTableService.updateUserAllInfo(allTableDto, allTableDto.getWriter_id());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "학생 체크인 수정하기")
    @PutMapping("/checkin")
    public ResponseEntity<Void> updateUserCheckInTable(@Parameter @RequestBody TableCheckInDto tableCheckInDto){
        allUserTableService.updateCheckIn(tableCheckInDto, tableCheckInDto.getTableDay());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "학생 체크아웃 수정하기")
    @PutMapping("/checkout")
    public ResponseEntity<Void> updateUserCheckOutTable(@Parameter @RequestBody TableCheckOutDto tableCheckOutDto){
        allUserTableService.updateCheckOut(tableCheckOutDto, tableCheckOutDto.getTableDay());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "학생 팀 수정하기 debugging용")
    @PutMapping("/userteam")
    public ResponseEntity<Void> updateUserTeam(@Parameter @RequestBody UserTeamChangeDto userTeamChangeDto,
                                                        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        allUserTableService.updateUserTeam(userTeamChangeDto, date == null? LocalDate.now() : date);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "학생 역할 수정하기 debugging용")
    @PutMapping("/userrole")
    public ResponseEntity<Void> updateUserRole(@Parameter @RequestBody UserRoleChangeDto userRoleChangeDto,
                                                        @Parameter(name = "date", description = "date=2022-02-11", in = ParameterIn.QUERY) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        allUserTableService.updateUserRole(userRoleChangeDto, date == null? LocalDate.now() : date);
        return ResponseEntity.ok().build();
    }

//    @Operation(description = "특정 학생들 정보 삭제하기")
//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteTodo(@Parameter(description = "삭제할 userId") @RequestBody Long id){
//        try{
 //           allUserTableService.delete(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e){
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
