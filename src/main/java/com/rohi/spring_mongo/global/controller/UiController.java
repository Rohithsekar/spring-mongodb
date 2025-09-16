package com.rohi.spring_mongo.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }
}