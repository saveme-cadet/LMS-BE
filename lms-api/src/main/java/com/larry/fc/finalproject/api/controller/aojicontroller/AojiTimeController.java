package com.larry.fc.finalproject.api.controller.aojicontroller;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.aojidto.AojiDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiResponseDto;
import com.larry.fc.finalproject.api.dto.aojidto.AojiUserDto;
import com.larry.fc.finalproject.api.service.aojiservice.AojiQuertService;
import com.larry.fc.finalproject.api.service.aojiservice.AojiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createAoji(@PathVariable(name = "userId") Integer userId) {//AuthUser authUser){
        aojiService.createAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 공부 끝")
    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateAoji( @PathVariable(name = "userId") Integer userId) {//AuthUser authUser){
        aojiService.updateAojiTime(userId.longValue());
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 공부 시간 수정")
    @PutMapping("/aojitime/{userId}")
    public ResponseEntity<Void> updateAojiTime(@PathVariable(name = "userId") Integer userId, @RequestBody AojiDto aojiDto){
        aojiService.updateAllAojiTime(userId.longValue(), aojiDto);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "aoji 보기")
    @GetMapping("/read/{userId}")
    public List<AojiResponseDto> readAoji(@PathVariable(name = "userId") Integer userId){
        return aojiQuertService.getAojiTime(userId.longValue());
    }
//
//    @Operation(description = "aoji status 보기")
//    @GetMapping("/read2/{userId}")
//    public String readAoji2(@PathVariable(name = "userId") Integer userId){
//        return aojiQuertService.getAojiTime2(userId.longValue());
//    }

    @Operation(description = "aoji 하는 사람")
    @GetMapping("/studyuser")
    public List<AojiUserDto> readAojiDoingUser(@PathVariable(name = "userId") Integer userId){
        return aojiQuertService.getAojiUser();
    }

    @Operation(description = "aoji 삭제")
    @DeleteMapping("/delete/{aojiIndex}")
    public ResponseEntity<Void> deleteAoji(@PathVariable(name = "userId") Integer userId, @PathVariable(name = "aojiIndex") Integer aojiIndex){
        try{
            aojiService.deleteAojiTime(userId.longValue(), aojiIndex.longValue());
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(description = "aoji 24시간 뒤에 삭제")
    @DeleteMapping("test")
    public ResponseEntity<Void> delete() {
        try {
            aojiService.deleteAojiTimeAtDay();
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
