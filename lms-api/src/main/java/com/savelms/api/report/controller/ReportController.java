package com.savelms.api.report.controller;

import com.savelms.api.report.dto.ReportResponse;
import com.savelms.api.report.service.ReportService;
import com.savelms.api.study_time.dto.StudyTimeResponse;
import com.savelms.core.user.domain.repository.UserRepository;
import com.savelms.core.user.role.RoleEnum;
import com.savelms.core.user.role.domain.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    //test6
    /*
    위험한 유저 보고
     */
    @Operation(description = "위험한 유저 보고")
    @GetMapping("/report-user")
    public ResponseEntity<List<ReportResponse>> reportDangerUser(@Parameter(hidden = true) @AuthenticationPrincipal User user) {

        /*
         validation 넣기

         user 권한 확인 후 role 이 manager 일 시 진행

         */
        Optional<com.savelms.core.user.domain.entity.User> user1 = userRepository.findByUsername(user.getUsername());

        if (user1.get().getUserRoles().equals(RoleEnum.ROLE_MANAGER)) {


            // reportRepository 에 있는 모든 유저 response
            List<ReportResponse> reportResponse = reportService.getDagerUser();

            return ResponseEntity.ok().body(reportResponse);
        } else
            return (ResponseEntity<List<ReportResponse>>) ResponseEntity.badRequest();
    }

}
