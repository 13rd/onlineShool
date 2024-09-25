package com.example.demo.service;


import com.example.demo.model.Course;
import com.example.demo.model.Report;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;

import com.example.demo.requests.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Cant find user with username: " + username);
        }
        return user.map(MyUserDetails::new).orElseThrow((() -> new UsernameNotFoundException(username + " not found")));
    }
    public boolean saveUser(UserModel user) {
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB.isPresent()) {
            return false;
        }
//        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void addReportToUser(Report report, UserModel user){
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        Optional<Report> reportFromDB = reportService.loadReportById(report.getId());
        if (userFromDB.isPresent() & reportFromDB.isPresent()) {
            user.addReport(reportFromDB.get());
            userRepository.save(user);

        }
    }

    public void subscribeToCourse(UserModel user, String courseId) {
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        Optional<Course> courseFromDB = courseService.loadCourseById(courseId);
         if (userFromDB.isPresent() & courseFromDB.isPresent() && courseService.addSub(courseFromDB.get().getId())) {
             user.addCourse(courseFromDB.get());
             userRepository.save(user);

        }

    }
    public void unsubscribeToCourse(UserModel user, String courseId) {
        Optional<UserModel> userFromDB = userRepository.findByUsername(user.getUsername());
        Optional<Course> courseFromDB = courseService.loadCourseById(courseId);
        if (userFromDB.isPresent() && courseFromDB.isPresent() && courseService.deleteSub(courseFromDB.get().getId())) {
            user.deleteCourse(courseFromDB.get());
            userRepository.save(user);

        }

    }


    public boolean deleteUser(String userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(String id) {
        return userRepository.findById(id);
    }


    public List<Course> getAllCourseInUser(String id){
        if (getUserById(id).isEmpty()){
            System.out.println("not user");  // redirect:/login
            return new ArrayList<>();
        }
        return getUserById(id).get().getCoursesEnrolled().stream().map(course -> courseService.loadCourseById(course).get()).collect(Collectors.toList());

    }

}
