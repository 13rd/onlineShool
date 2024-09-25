package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String showUserById(@PathVariable String id, Model model) {
        UserModel user = userService.getUserById(id).orElse(null);
        model.addAttribute("user", user);
        System.out.println(user);
        if (user == null){
            return "redirect:/login11";
        }
        List<Course> courses = userService.getAllCourseInUser(id);
        model.addAttribute("enrolledCourses", courses);
        System.out.println(courses);
        if (user.getRole().equals("STUDENT")){
            return "student";
        }
        if (user.getRole().equals("TEACHER")){
            return "teacher";
        }
        return "index";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        System.out.println(userDetails);
        if (userDetails == null) {
            return "redirect:/error";
        }
        UserModel user = userService.getUserById(userDetails.getUserId()).orElse(null);
        model.addAttribute("user", user);
//        List<Course> courses = userDetails.;
        List<Course> courses = userService.getAllCourseInUser(user.getId());
        model.addAttribute("enrolledCourses", courses);
        model.addAttribute("authenticated", user!=null);
//        System.out.println(courses);
        if (user.getRole().equals("STUDENT")){
            return "student";
        }
        if (user.getRole().equals("TEACHER")){
            return "teacher";
        }
        return "index";
    }



}

//    @GetMapping("/users/{id}")
//    public String showUserById(@PathVariable String id, Model model) {
//        UserModel user = userService.getUserById(id).orElse(null);
//        model.addAttribute("user", user);
//        System.out.println(user);
//        if (user == null){
//            return "redirect:/login11";
//        }
//        List<Course> courses = userService.getAllCourseInUser(id);
//        model.addAttribute("enrolledCourses", courses);
//        System.out.println(courses);
//        if (user.getRole().equals("STUDENT")){
//            return "student";
//        }
//        if (user.getRole().equals("TEACHER")){
//            return "teacher";
//        }
//        return "index";
//    }
//
//    @GetMapping("/profile")
//    public String profile(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
//        if (userDetails == null) {
//            return "redirect:/error";
//        }
//        String userId = userDetails.getUserId();
//        return "redirect:/users/" + userId;
//    }