package com.example.demo.controller;

import com.example.demo.service.LoginService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    @Autowired
    private LoginService loginService;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String showLogPage() {
        System.out.println("START LOGIN");
        return "login";
    }

    @PostMapping
    public String login(@RequestParam (required = true, defaultValue = "!@") String username,
                        @RequestParam (required = true, defaultValue = "!@") String password) {
        System.out.println("Login");
        if(!(username.equals("!@") & password.equals("!@"))) {
            System.out.println("Неполные учетные данные");
        }
        if (loginService.authenticate(username, password)) {
            // JWT
            System.out.println("Успешная аутентификация");
            return "redirect:/";
        } else {
            System.out.println("Неверные учетные данные");
        }
        return "redirect:/login";
    }
}
