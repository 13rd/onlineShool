package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String showRegPage() {
        return "register";
    }

    @PostMapping
    public String registerUser(@RequestParam (required = true, defaultValue = "!@") String username,
                               @RequestParam (required = true, defaultValue = "!@") String name,
                               @RequestParam (required = true, defaultValue = "!@") String surname,
                               @RequestParam (required = true, defaultValue = "!@") String password,
                               @RequestParam (required = true, defaultValue = "!@") String email,
                               @RequestParam (required = true, defaultValue = "!@") String role) {
        if(!(name.equals("!@") & surname.equals("!@") & password.equals("!@") & email.equals("!@") & role.equals("!@"))) {
            System.out.println(name + " " + surname + " " + password);
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            user.setEmail(email);
            user.setRole(role);
            //TODO: diff params
            if(userService.saveUser(user))
                System.out.println("GOOD");
            else
                System.out.println("BAD");
        }
        return "redirect:/";
    }

}