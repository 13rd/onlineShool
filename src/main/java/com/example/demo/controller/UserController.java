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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LoginService loginService;
    @PostMapping("/subscribe")
    public String subscribeById(@RequestParam (required = true, defaultValue = "!@") String courseId, @AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if(courseId==null){
            return "redirect:/error";
        }
        Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
        if (user.isEmpty()){
            return "redirect:/login";
        }
        userService.subscribeToCourse(user.get(), courseId);
        return "redirect:/";
    }

}

