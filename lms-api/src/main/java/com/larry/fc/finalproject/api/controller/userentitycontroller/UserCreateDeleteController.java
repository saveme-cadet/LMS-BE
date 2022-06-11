package com.larry.fc.finalproject.api.controller.userentitycontroller;

import com.larry.fc.finalproject.api.dto.userdto.UserDto;
import com.larry.fc.finalproject.api.service.userservice.UserQueryService;
import com.larry.fc.finalproject.core.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "http://15.165.148.236:8080")
@Tag(name = "유저 생성, 삭제")
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserCreateDeleteController {
    private final UserService userService;
    private final UserQueryService userQueryService;

    @Operation(description = "특정 유저 삭제")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Integer userId) {
        try {
            userService.delete(userId.longValue());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(description = "참가 하는 모든 유저 Id 가져 오기")
    @GetMapping("/alluser")
    public List<UserDto> allUserGet () {
            return userQueryService.getUser();
    }
}
