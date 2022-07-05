package com.larry.fc.finalproject.api.controller.utilcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.larry.fc.finalproject.api.service.LoginService.LOGIN_SESSION_KEY;

@RequestMapping("/")
@RestController
public class IndexController {

    @GetMapping()
    public String index() {
        return "hello";
    }
}