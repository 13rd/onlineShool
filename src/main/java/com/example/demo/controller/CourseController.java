package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.requests.MyUserDetails;
import com.example.demo.service.CourseService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserService userService;


//    @GetMapping("/")
//    public String index(Model model){
//        List<Course> courses = courseRepository.findAll();
//        model.addAttribute("courses", courses);
//        return "index";
//    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "index";
    }

    @GetMapping("/courses/{id}")
    public String getCourseById(@PathVariable  String id, @AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        Optional<Course> course = courseService.loadCourseById(id);
        if (course.isEmpty()){
            return "redirect:/";
        }
        List<Lesson> lessons = lessonRepository.findByCourseId(id);
        Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
        Optional<UserModel> teacher = userService.getUserById(course.get().getCreatorId());
        if (user.isEmpty()){
            return "redirect:/login";
        }

        if (teacher.isEmpty()){
            return "redirect:/error";
        }
        model.addAttribute("user", user.get());
        model.addAttribute("course", course.get());
        model.addAttribute("teacher", teacher.get());
        model.addAttribute("lessons", lessons);
        return "course";
    }
}
