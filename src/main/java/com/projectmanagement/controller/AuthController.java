package com.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {

        System.out.println("====================login====Rashed ============");
        return "auth/login";

    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}