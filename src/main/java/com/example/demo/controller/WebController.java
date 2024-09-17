package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {


    @Autowired
    private UserService userService;

//    @GetMapping("/login")
//    public String login(Model model){
//        return "login";
//    }

//    @GetMapping("/register")
//    public String register(Model model){
//        return "signup";
//    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("courses", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable String id, Model model) {
        User user = userService.getUserById(id).orElse(null);
        model.addAttribute("user", user);

        if (user == null){
            return "home";
        }
        if (user.getRole().equals("student")){
            return "student";
        }
        if (user.getRole().equals("teacher")){
            return "teacher";
        }
        return "home";
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
