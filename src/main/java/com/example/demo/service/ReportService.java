package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.Report;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requests.MyUserDetails;
import com.example.demo.requests.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ReportRepository reportRepository;

//    @Autowired
//    private UserService userService;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getAllReportsFromUser(String userId){
//        UserModel user = userService.getUserById(userId).get();
        List<Report> allReports = getAllReports();
        List<Report> userReports = new ArrayList<>();
        for (Report report: allReports) {
            if (report.getStudentId().equals(userId)){
                userReports.add(report);
            }
        }
        return userReports;
    }
    public List<Report> getAllReportsForTeacher(String teacherId){
//        UserModel teacher = userService.getUserById(teacherId).get();
        List<Report> allReports = getAllReports();
        System.out.println("all reports"+allReports);
        List<Report> teacherReports = new ArrayList<>();
        for (Report report: allReports) {
            if (report.getTeacherId().equals(teacherId)){
                teacherReports.add(report);
            }
        }
        return teacherReports;
    }

    public Optional<Report> loadReportById(String id) {
        List<Report> allReports = getAllReports();
        for (Report report: allReports) {
            if(report.getId().equals(id)){
                return Optional.of(report);
            }
        }

        throw new IllegalArgumentException("Cant find Report with id: " + id);
    }


    public boolean saveReport(Report report) {
//        Optional<Report> reportFromDB = loadReportById(report.getId());
//        if (reportFromDB.isPresent()) {
//            return false;
//        }
        reportRepository.save(report);
        return true;
    }

    public boolean rejectReport(String reportId){
        Optional<Report> reportFromDB = loadReportById(reportId);
        if (reportFromDB.get().getReportStatus().equals(ReportStatus.ONCHECK)){
            reportFromDB.get().setReportStatus(ReportStatus.REJECTED);
            reportRepository.save(reportFromDB.get());
            return true;
        }
        return false;

    }
    public boolean verifyReport(String reportId){
        Optional<Report> reportFromDB = loadReportById(reportId);
        if (reportFromDB.get().getReportStatus().equals(ReportStatus.ONCHECK)){
            reportFromDB.get().setReportStatus(ReportStatus.VERIFIED);
            reportRepository.save(reportFromDB.get());
            return true;
        }
        return false;
    }
}
