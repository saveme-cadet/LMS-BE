package com.savelms.api.root;

import com.savelms.api.todo.controller.dto.ListResponse;
import com.savelms.api.user.controller.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class rootGetMapping {
    @GetMapping("/spring")
    public String helloSpring(){
        return "hello Spring";
    }

}
