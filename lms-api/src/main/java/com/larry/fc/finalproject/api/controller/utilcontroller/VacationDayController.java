package com.larry.fc.finalproject.api.controller.utilcontroller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.PlusVacationDto;
import com.larry.fc.finalproject.api.service.utilservice.PlusVacationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "http://15.165.148.236:8080")
@Tag(name = "휴가 테스트 위한 api")
@RequiredArgsConstructor
@RequestMapping("/api/day")
@RestController
public class VacationDayController {

    private final PlusVacationService plusVacationService;

    @Operation(description = "날짜 생성 테스트를 위한 create 따로 생성 안해도 됌. 머슴으로 변경  시키면 자동 생성")
    @PostMapping("/create")
    public ResponseEntity<Void> createUserInfo(@Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        plusVacationService.create(authUser);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "현재 머슴인 id의 머슴 시작 요일과 끝을 보여줌")
    @GetMapping("/show")
    public List<PlusVacationDto> getUserInfoByDay(
            @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){

        return plusVacationService.getUserInfoByDay(authUser);
    }

    @Operation(description = "머슴 에서 카뎃이 될때 자동적으로 휴가 +1로해놨는데 7일 간격으로 했기 때문에 현실적으로 EndAt요일 수정 불가로 인해서 만듬")
    @PutMapping("/vacation")
    public ResponseEntity<Void> updateUserCheckInTable(@Parameter @RequestBody PlusVacationDto plusVacationDto, @Parameter(name = "userId", description = "user 의 id", in = ParameterIn.QUERY) AuthUser authUser){
        plusVacationService.update(plusVacationDto, authUser);
        return ResponseEntity.ok().build();
    }


}
