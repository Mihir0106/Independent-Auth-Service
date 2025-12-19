package com.Independent.AuthService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/req/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/req/signup")
    public  String showSignUpPage(){
        return "signup";
    }

    @GetMapping("/index")
    public String showHomePage(){
        return "index";
    }
}
