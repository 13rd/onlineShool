package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.Lesson;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
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
public class LessonService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;


    public Optional<Lesson> loadLessonById(String id) {
        List<Lesson> allLessons = getAllLessons();
        for (Lesson lesson: allLessons) {
            if(lesson.getId().equals(id)){
                return Optional.of(lesson);
            }
        }

        throw new IllegalArgumentException("Cant find lesson with id: " + id);
//        return Optional.empty();
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public boolean saveLesson(Lesson lesson) {
//        Optional<Course> courseFromDB = loadCourseById(course.getId());
//        if (courseFromDB.isPresent()) {
//            return false;
//        }
        lessonRepository.save(lesson);
        return true;
    }

    public void deleteLesson(Lesson lesson){
        lessonRepository.delete(lesson);
    }

}
