package com.example.bi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController

public class User {

    @GetMapping("/user")
    public String getUser(){
        return "Hello User";
    }

}
