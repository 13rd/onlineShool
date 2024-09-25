package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.requests.MyUserDetails;
import com.example.demo.service.CourseService;
import com.example.demo.service.LoginService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebController {


    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String index(SecurityContextHolderAwareRequestWrapper request, Model model){
        model.addAttribute("authenticated", request.getUserPrincipal() != null);
        model.addAttribute("courses", courseService.getAllCourses());
//        model.addAttribute("user", userService.getAllUsers());
        return "index";
    }





    @GetMapping("/error/{message}")
    public String errorPage(@PathVariable String message, Model model){
        model.addAttribute("errorMessage", message);
        return "error";
    }
}
