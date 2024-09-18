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
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String index(SecurityContextHolderAwareRequestWrapper request, Model model){
        model.addAttribute("authenticated", request.getUserPrincipal() != null);
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("user", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String userId = userDetails.getUserId();
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("courses", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable String id, Model model) {
        UserModel user = userService.getUserById(id).orElse(null);
        model.addAttribute("user", user);

        if (user == null){
            return "index";
        }
        List<Course> courses = user.getCoursesEnrolled().stream().map(course -> courseService.loadCourseById(course).get()).collect(Collectors.toList());
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

    @GetMapping("/error/{message}")
    public String errorPage(@PathVariable String message, Model model){
        model.addAttribute("errorMessage", message);
        return "error";
    }

//    @GetMapping("/books")
//    public String getAllBooks(Model model) {
//        model.addAttribute("books", bookService.getAllBooks());
//        return "book-list";
//    }
//
//    @GetMapping("/new")
//    public String showNewBookForm(Model model) {
//        model.addAttribute("book", new Book());
//        return "new-book";
//    }
//
//    @PostMapping
//    public String saveBook(@ModelAttribute("book") Book book) {
//        bookService.saveBook(book);
//        return "redirect:/books";
//    }
}
