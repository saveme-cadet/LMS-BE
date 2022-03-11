package com.larry.fc.finalproject.api.controller.aojicontroller;

import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.service.aojiservice.AojiQuertService;
import com.larry.fc.finalproject.api.service.aojiservice.AojiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "아오지 탄광")
@RequiredArgsConstructor
@RequestMapping("/api/aoji")
@RestController
public class AojiTimeController {
    private final AojiService aojiService;
    private final AojiQuertService aojiQuertService;

    @Operation(description = "aoji 공부 시작")
    @PostMapping("/create/{userId}")
    public ResponseEntity<Void> createAoji(@PathVariable(name = "userId") Integer userId){
        aojiService.createAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 공부 끝")
    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateAoji(@PathVariable(name = "userId") Integer userId){
        aojiService.updateAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 공부 시간 수정")
    @PutMapping("/aojitime/{userId}")
    public ResponseEntity<Void> updateAojiTime(@PathVariable(name = "userId") Integer userId){
        aojiService.updateAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 보기")
    @GetMapping("/read/{userId}")
    public List<AojiResponseDto> readAoji(@PathVariable(name = "userId") Integer userId){
        return aojiQuertService.getAojiTime(userId.longValue());
    }

    @Operation(description = "aoji 하는 사람")
    @GetMapping("/studyuser")
    public ResponseEntity<AojiResponseDto> readAojiDoingUser(){
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 삭제")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteAoji(@PathVariable(name = "userId") Integer userId){
        aojiService.deleteAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }
}
