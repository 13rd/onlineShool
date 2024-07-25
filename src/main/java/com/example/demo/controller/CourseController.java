package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/")
    public String index(Model model){
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "index";
    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "index";
    }

    @GetMapping("/courses/{id}")
    public String getCourseById(@PathVariable String id, Model model) {
        Course course = courseRepository.findById(id).orElse(null);
        List<Lesson> lessons = lessonRepository.findByCourseId(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        return "course";
    }
}
