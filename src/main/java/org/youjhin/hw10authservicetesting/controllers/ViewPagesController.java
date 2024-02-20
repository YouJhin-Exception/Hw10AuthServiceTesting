package org.youjhin.hw10authservicetesting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewPagesController {

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/errorpage")
    public String errorPage() {
        return "errorpage";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
