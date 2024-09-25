package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.Report;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.requests.MyUserDetails;
import com.example.demo.service.CourseService;
import com.example.demo.service.LessonService;
import com.example.demo.service.ReportService;
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
public class ReportController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;


    @PostMapping("/report")
    public String sendReport(@RequestParam (required = true, defaultValue = "!@") String lessonId,
                             @RequestParam (required = true, defaultValue = "!@") String userId,
                             @RequestParam (required = true, defaultValue = "!@") String homeworkAnswer,
                             @AuthenticationPrincipal MyUserDetails userDetails){

        if(lessonService.loadLessonById(lessonId).isPresent() && userService.getUserById(userId).isPresent() && !homeworkAnswer.equals("!@")){
            Report report = new Report();
            report.setLessonId(lessonId);
            report.setStudentId(userId);
            report.setTeacherId(courseService.loadCourseById(lessonService.loadLessonById(lessonId).get().getCourseId()).get().getCreatorId());
            report.setMessage(homeworkAnswer);
            if(reportService.saveReport(report)) {
                System.out.println("GOOD");
                userService.addReportToUser(report, userService.getUserById(userId).get());
            }
            else
                System.out.println("BAD");
        }

        return "redirect:/lesson/"+lessonId;
    }

    @GetMapping("/messages")
    public String showMessages(@AuthenticationPrincipal MyUserDetails userDetails, Model model){
        if(userService.getUserById(userDetails.getUserId()).get().getRole().equals("STUDENT")){
            model.addAttribute("messages", reportService.getAllReportsFromUser(userDetails.getUserId()));
            return "student_messages";
        }
        if(userService.getUserById(userDetails.getUserId()).get().getRole().equals("TEACHER")){
            model.addAttribute("messages", reportService.getAllReportsForTeacher(userDetails.getUserId()));
            return "teacher_messages";
        }
        return "redirect:/profile";
    }

    @GetMapping("/reject_report/{id}")
    public String rejectReport(@PathVariable String id){
        reportService.rejectReport(id);
        return "redirect:/messages";
    }
    @GetMapping("/verify_report/{id}")
    public String verifyReport(@PathVariable String id){
        reportService.verifyReport(id);
        return "redirect:/messages";
    }
}
