package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.requests.MyUserDetails;
import com.example.demo.service.CourseService;
import com.example.demo.service.LessonService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CourseController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/create_course")
    public String showCreateCoursePage(Model model){
        return "create_course";
    }

    @PostMapping("/create_course")
    public String createCourse(@RequestParam (required = true, defaultValue = "!@") String title,
                               @RequestParam (required = true, defaultValue = "!@") String description,
                               @RequestParam (required = true, defaultValue = "0") Integer count,
                               @AuthenticationPrincipal MyUserDetails userDetails){
        if(!(title.equals("!@") && description.equals("!@") && count.equals("0")) && count > 0) {
//            System.out.println(title + " " + description + " " + count);
            Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setMaxStudents(count);
            course.setCreatorId(userDetails.getUserId());
            //TODO: diff params
            if(courseService.saveCourse(course)) {
                System.out.println("GOOD");
                userService.subscribeToCourse(user.get(), course.getId());
            }
            else
                System.out.println("BAD");

        }
        return "redirect:/profile";
    }

    @PostMapping("/edit_course")
    public String editCourse(@RequestParam (required = true, defaultValue = "!@") String courseId,
                             @RequestParam (required = true, defaultValue = "!@") String title,
                             @RequestParam (required = true, defaultValue = "!@") String description,
                             @RequestParam (required = true, defaultValue = "0") Integer count,
                             @AuthenticationPrincipal MyUserDetails userDetails){
        if(!(title.equals("!@") & description.equals("!@") & count.equals("0")) & courseService.loadCourseById(courseId).isPresent()) {
            System.out.println(title + " " + description + " " + count);
            Course course = courseService.loadCourseById(courseId).get();
            course.setTitle(title);
            course.setDescription(description);
            course.setStudentsCount(count);
            //TODO: diff params
            if(courseService.saveCourse(course)) {
                System.out.println("GOOD");
            }
            else
                System.out.println("BAD");

        }
        return "redirect:/profile";
    }

    @GetMapping("/edit_course/{id}")
    public String editCoursePage(@AuthenticationPrincipal MyUserDetails userDetails,
                                 @PathVariable String  id,
                                 Model model){
        Optional<Course> course = courseService.loadCourseById(id);
        List<Lesson> lessons = courseService.getAllLessonInCourse(id);
        System.out.println(lessons);
//        Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
        model.addAttribute("course", course.get());
        model.addAttribute("lessons", lessons);
        return "edit_course";
    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "index";
    }

    @GetMapping("/courses/{id}")
    public String getCourseById(@PathVariable String id, @AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        Optional<Course> course = courseService.loadCourseById(id);
        if (course.isEmpty()){
            return "redirect:/";
        }
        List<Lesson> lessons = lessonRepository.findByCourseId(id);
        Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
        Optional<UserModel> teacher = userService.getUserById(course.get().getCreatorId());
        model.addAttribute("userSubscribed", user.get().getCoursesEnrolled().contains(course.get().getId()));
        model.addAttribute("authenticated", user.isPresent());
        System.out.println(user.get().getCoursesEnrolled().contains(course.get()));
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
