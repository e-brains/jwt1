package com.kye.jwt1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/home")
    public String home(){
        return "<h1>home</ha>";
    }

    @PostMapping("/token")
    public String token(){
        return "<h1>token</ha>";
    }
}