package com.example.demo.repository;

import com.example.demo.model.Lesson;
import com.example.demo.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {
//    Report findById(String id);
}
