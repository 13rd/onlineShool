package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requests.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Optional<Course> loadCourseById(String id) {
        List<Course> allCourses = getAllCourses();
        for (Course course: allCourses) {
            if(course.getId().equals(id)){
                return Optional.of(course);
            }
        }

        throw new IllegalArgumentException("Cant find course with id: " + id);
//        return Optional.empty();
    }
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
