package com.savelms.api.root;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class rootGetMapping implements ErrorController {
    @GetMapping("/spring")
    public String helloSpring(){

        return "hello Spring6";
    }

    @RequestMapping("/error")
    public String error() {
        return "error";
    }

}
