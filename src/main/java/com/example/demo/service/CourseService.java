package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requests.MyUserDetails;
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
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonService lessonService;

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

    public List<Lesson> getAllLessonInCourse(String id){
        if (loadCourseById(id).isEmpty()){
            return new ArrayList<>();
        }
        return loadCourseById(id).get().getLessons().stream().map(lesson -> lessonService.loadLessonById(lesson).get()).collect(Collectors.toList());

    }

    public boolean addSub(String courseId){
        Course course = loadCourseById(courseId).get();
        if (course.getStudentsCount() < course.getMaxStudents()){
            course.setStudentsCount(course.getStudentsCount()+1);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    public boolean deleteSub(String courseId){
        Course course = loadCourseById(courseId).get();
        if (course.getStudentsCount() > 0){
            course.setStudentsCount(course.getStudentsCount()-1);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    public boolean saveCourse(Course course) {
//        Optional<Course> courseFromDB = loadCourseById(course.getId());
//        if (courseFromDB.isPresent()) {
//            return false;
//        }
        courseRepository.save(course);
        return true;
    }

    public void addLessonToCourse(Lesson lesson, String courseId) {
        Course course = loadCourseById(courseId).get();
        course.addLesson(lesson);
        courseRepository.save(course);

    }
}
