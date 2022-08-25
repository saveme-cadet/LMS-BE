package com.savelms.api.vacation.controller;

import com.savelms.api.vacation.dto.AddVacationRequest;
import com.savelms.api.vacation.dto.UseVacationRequest;
import com.savelms.api.vacation.dto.VacationReasonResponse;
import com.savelms.api.vacation.dto.VacationResponse;
import com.savelms.api.vacation.service.VacationService;
import com.savelms.core.exception.ExceptionResponse;
import com.savelms.core.exception.VacationNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;


    /**
     * 생성
     * */
    @PreAuthorize("hasAuthority('vacation.create') and hasAuthority('vacation.update')")
    @Operation(description = "휴가 추가")
    @PostMapping("/users/{userApiId}/vacations/added-days")
    public ResponseEntity<VacationResponse> addVacation(@RequestBody @Valid AddVacationRequest vacationRequest,
                                                        @PathVariable("userApiId") String userApiId) {
        VacationResponse vacationResponse = vacationService.addVacation(vacationRequest, userApiId);

        return ResponseEntity.ok().body(vacationResponse);
    }

    //휴가를 사용할 때 마다 INSERT(사용 이유를 기록하기 위해)
    @PreAuthorize("hasAuthority('user.vacation.create')")
    @Operation(description = "휴가 사용")
    @PostMapping("/users/{userApiId}/vacations/used-days")
    public ResponseEntity<VacationResponse> useVacation(@RequestBody @Valid UseVacationRequest vacationRequest,
                                                        @PathVariable("userApiId") String userApiId) {
        VacationResponse vacationResponse = vacationService.useVacation(vacationRequest, userApiId);

        return ResponseEntity.ok().body(vacationResponse);
    }


    /**
     * 조회
     * */
    @PreAuthorize("hasAuthority('user.vacation.read')")
    @Operation(description = "남은 휴가 조회")
    @GetMapping("/users/{userApiId}/vacations/remaining-vacations")
    public ResponseEntity<VacationResponse> getRemainingVacation(@PathVariable("userApiId") String userApiId) {
        VacationResponse vacationResponse = vacationService.getRemainingVacation(userApiId);

        return ResponseEntity.ok().body(vacationResponse);
    }


    @PreAuthorize("hasAuthority('user.vacation.read')")
    @Operation(description = "사용한 휴가 이력 조회")
    @GetMapping("/users/{userApiId}/vacations/used-vacations")
    public ResponseEntity<List<VacationReasonResponse>> getUsedVacation(@PathVariable("userApiId") String userApiId) {
        List<VacationReasonResponse> vacationResponses = vacationService.getUsedVacation(userApiId);

        return ResponseEntity.ok().body(vacationResponses);
    }


    /**
     * 예외처리
     * 임시로 API 컨트롤로에서 처리
     * 나중에 ControllerAdvice에서 처리하도록 변경
     * */
    @ExceptionHandler(VacationNotFoundException.class)
    public ResponseEntity<String> handle(VacationNotFoundException e, HttpServletRequest request) {
        log.error(e.getClass().getName(), e.getMessage());

        String exceptionDir = e.getClass().getName();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(exceptionDir.substring(exceptionDir.lastIndexOf(".") + 1));
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }
}