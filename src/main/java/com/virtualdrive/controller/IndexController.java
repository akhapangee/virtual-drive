package com.virtualdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/login", "/"})
public class IndexController {
    @GetMapping
    public String loginPage() {
        return "login";
    }
}
