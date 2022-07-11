package com.savelms.api.vacation.controller;

import com.savelms.api.vacation.dto.AddVacationRequest;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.dto.VacationReasonResponse;
import com.savelms.api.vacation.dto.VacationResponse;
import com.savelms.api.vacation.service.VacationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;


    /**
     * 생성
     * */
    //휴가를 사용할 때 마다 INSERT(사용 이유를 기록하기 위해)
    @Operation(description = "휴가 사용")
    @PostMapping("/vacations")
    public ResponseEntity<VacationResponse> useVacation(@RequestBody @Valid UseVacationRequest vacationRequest,
                                                        @AuthenticationPrincipal User user) {
        VacationResponse vacationResponse = vacationService.useVacation(vacationRequest, user.getUsername());

        return ResponseEntity.ok().body(vacationResponse);
    }


    /**
     * 조회
     * */
    @Operation(description = "남은 휴가 조회")
    @GetMapping("/remaining-vacations")
    public ResponseEntity<VacationResponse> getRemainingVacation(@AuthenticationPrincipal User user) {
        VacationResponse vacationResponse = vacationService.getRemainingVacation(user.getUsername());

        return ResponseEntity.ok().body(vacationResponse);
    }

    @Operation(description = "사용한 휴가 이력 조회")
    @GetMapping("/used-vacations")
    public ResponseEntity<List<VacationReasonResponse>> getUsedVacation(@AuthenticationPrincipal User user) {
        List<VacationReasonResponse> vacationResponses = vacationService.getUsedVacation(user.getUsername());

        return ResponseEntity.ok().body(vacationResponses);
    }



    /**
     * 수정
     * */
    @Operation(description = "휴가 일수 추가")
    @PatchMapping("/vacations")
    public ResponseEntity<VacationResponse> addVacation(@RequestBody @Valid AddVacationRequest vacationRequest,
                                                        @RequestParam Long userId) {
        VacationResponse vacationResponse = vacationService.addVacation(vacationRequest, userId);

        return ResponseEntity.ok().body(vacationResponse);
    }

}
