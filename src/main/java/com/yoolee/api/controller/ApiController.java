package com.yoolee.api.controller;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableResourceServer
@RestController
public class ApiController {

    @RequestMapping("/main")
    public String main(){
        return "hi";
    }
}
