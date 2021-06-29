package com.kye.jwt1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "/index";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/loginForm";
    }

}
