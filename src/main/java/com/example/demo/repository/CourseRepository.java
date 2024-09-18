package com.example.demo.repository;

import com.example.demo.model.Course;
import com.example.demo.model.UserModel;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
//    Optional<Course> findById(String id);
}