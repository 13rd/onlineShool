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

@Controller
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/create_lesson/{courseId}")
    public String showCreateLessonPage(@PathVariable String courseId, Model model){
        model.addAttribute("courseId", courseId);
        return "create_lesson";
    }

    @PostMapping("/create_lesson")
    public String createLesson(@RequestParam (required = true, defaultValue = "!@") String courseId,
                               @RequestParam (required = true, defaultValue = "!@") String title,
                               @RequestParam (required = true, defaultValue = "!@") String description,
                               @RequestParam (required = true, defaultValue = "!@") String content,
                               @RequestParam (required = true, defaultValue = "!@") String homework,
                               @AuthenticationPrincipal MyUserDetails userDetails){
        if(!(title.equals("!@") & description.equals("!@") & content.equals("!@") & homework.equals("!@")) & courseService.loadCourseById(courseId).isPresent()) {
//            Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
            Lesson lesson = new Lesson();
            lesson.setTitle(title);
            lesson.setDescription(description);
            lesson.setContent(content);
            lesson.setHomework(homework);
            lesson.setCourseId(courseId);
            //TODO: diff params
            if(lessonService.saveLesson(lesson)) {
                System.out.println("GOOD");
                courseService.addLessonToCourse(lesson, courseId);
            }
            else
                System.out.println("BAD");

        }
        return "redirect:/edit_course/"+courseId;
    }

    @GetMapping("/edit_lesson/{id}")
    public String showEditLessonPage(@PathVariable String id, Model model){
        model.addAttribute("lesson", lessonService.loadLessonById(id).get());
        return "edit_lesson";
    }

    @PostMapping("/edit_lesson")
    public String editLesson(@RequestParam (required = true, defaultValue = "!@") String id,
                               @RequestParam (required = true, defaultValue = "!@") String title,
                               @RequestParam (required = true, defaultValue = "!@") String description,
                               @RequestParam (required = true, defaultValue = "!@") String content,
                               @RequestParam (required = true, defaultValue = "!@") String homework,
                               @AuthenticationPrincipal MyUserDetails userDetails){
        if(!(title.equals("!@") && description.equals("!@") && content.equals("!@") && homework.equals("!@")) && lessonService.loadLessonById(id).isPresent()) {
//            Optional<UserModel> user = userService.getUserById(userDetails.getUserId());
            Lesson lesson = lessonService.loadLessonById(id).get();
            lesson.setTitle(title);
            lesson.setDescription(description);
            lesson.setContent(content);
            lesson.setHomework(homework);
            if(lessonService.saveLesson(lesson)) {
                System.out.println("GOOD");
                courseService.loadCourseById(lesson.getCourseId()).get().addLesson(lesson);
            }
            else
                System.out.println("BAD");

        }
        return "redirect:/edit_course/"+lessonService.loadLessonById(id).get().getCourseId();
    }

    @PostMapping("/delete_lesson/{id}")
    public String deleteLesson(@RequestParam (required = true, defaultValue = "!@") String id){
        if(lessonService.loadLessonById(id).isPresent()){
            lessonService.deleteLesson(lessonService.loadLessonById(id).get());
        }
        return "redirect:/edit_course/"+lessonService.loadLessonById(id).get().getCourseId();
    }


    @GetMapping("/lesson/{id}")
    public String getLessonById(@PathVariable String id,
                                @AuthenticationPrincipal MyUserDetails userDetails,
                                Model model) {
        Optional<Lesson> lesson = lessonService.loadLessonById(id);
        if (lesson.isEmpty()){
            return "redirect:/";
        }

        model.addAttribute("lesson", lesson.get());
        model.addAttribute("user", userService.getUserById(userDetails.getUserId()).get());
        return "lesson";
    }
}
