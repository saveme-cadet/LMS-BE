package com.larry.fc.finalproject.api.controller.utilcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.larry.fc.finalproject.api.service.LoginService.LOGIN_SESSION_KEY;

@CrossOrigin(originPatterns = "http://15.165.148.236:8080")
@RequestMapping("/")
@RestController
public class IndexController {

    @GetMapping()
    public String index() {
        return "hello";
    }
}
